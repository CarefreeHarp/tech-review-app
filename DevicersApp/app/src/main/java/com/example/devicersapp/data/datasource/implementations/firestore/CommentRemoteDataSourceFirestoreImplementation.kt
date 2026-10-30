package com.example.devicersapp.data.datasource.implementations.firestore
import com.example.devicersapp.data.datasource.*
import com.example.devicersapp.ui.models.*
import com.example.devicersapp.data.dto.ResponseDTO.firestore.*
import com.example.devicersapp.data.dto.ResponseDTO.shared.*
import com.example.devicersapp.data.dto.RequestDTO.firestore.*
import com.google.firebase.firestore.*
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/** Implementa las operaciones de comentarios con las rutas y campos de Firestore v9. */
class CommentRemoteDataSourceFirestoreImplementation @Inject constructor(
    private val firestore: FirebaseFirestore
) : CommentRemoteDataSource {
    private val sequence get() = firestore.collection("counters").document("next_id")

    override suspend fun getComments(reviewId: Int?): List<CommentInfo> {
        val reviews = if (reviewId != null) listOf(firestore.collection("reviews").document(reviewId.toString()))
            else firestore.collection("reviews").get(Source.SERVER).await().documents.map { it.reference }
        return reviews.flatMap { review ->
            review.collection("comments").get(Source.SERVER).await().documents.map {
                it.toCommentFirestoreDto().toCommentInfo(review.id.toInt())
            }
        }
    }

    /**
     * Publica el comentario dentro de una transacción: el documento en reviews/{reviewId}/comments, el
     * contador comments_count de la reseña y la secuencia de IDs se guardan juntos, o no se aplica ninguno.
     */
    override suspend fun createComment(draft: CommentDraft): CommentInfo {
        require(draft.reviewId > 0 && draft.userId > 0 && draft.body.isNotBlank())
        draft.parentCommentId?.let { require(it > 0) }
        val reviewRef = firestore.collection("reviews").document(draft.reviewId.toString())
        val comments = reviewRef.collection("comments")
        val authorRef = firestore.collection("users").document(draft.userId.toString())
        val firstCommentId = commentIdSeed()
        val id = firestore.runTransaction { transaction ->
            // Firestore exige completar todas las lecturas de la transacción antes de la primera escritura.
            val review = transaction.get(reviewRef)
            check(review.exists() && review.getBoolean("is_active") == true) { "La reseña ya no está disponible." }
            val author = transaction.get(authorRef)
            check(author.exists()) { "No existe el perfil del autor del comentario." }
            draft.parentCommentId?.let { parentId ->
                val parent = transaction.get(comments.document(parentId.toString()))
                check(parent.exists() && parent.getBoolean("is_active") == true) { "No existe el comentario al que responde." }
            }
            var nextCommentId = transaction.get(sequence).getLong("next_comment_id")?.toInt() ?: firstCommentId
            check(nextCommentId > 0)
            while (transaction.get(comments.document(nextCommentId.toString())).exists()) {
                check(nextCommentId < Int.MAX_VALUE) { "Se agotaron los IDs de comentario." }
                nextCommentId++
            }
            // Copia el nombre y la foto del autor; la Cloud Function syncUserInfoInComments los mantiene al día.
            val comment = draft.toCreateCommentFirestoreDto(
                username = checkNotNull(author.getString("username")) { "El autor no tiene nombre de usuario." },
                profileImageUrl = author.getString("profile_image_url")
            )
            transaction.set(comments.document(nextCommentId.toString()), comment.fields(nextCommentId))
            transaction.update(reviewRef, "comments_count", FieldValue.increment(1))
            // merge conserva las secuencias de usuarios, reseñas y notificaciones en este mismo documento.
            transaction.set(sequence, mapOf("next_comment_id" to nextCommentId.toLong() + 1), SetOptions.merge())
            nextCommentId
        }.await()
        // La lectura posterior trae las fechas que asignó el servidor.
        return comments.document(id.toString()).get(Source.SERVER).await()
            .toCommentFirestoreDto().toCommentInfo(draft.reviewId)
    }

    /** Primer ID disponible cuando counters/next_id aún no tiene la secuencia de comentarios. */
    private suspend fun commentIdSeed(): Int {
        sequence.get(Source.SERVER).await().getLong("next_comment_id")?.let { return it.toInt() }
        // Los comentarios viven en subcolecciones: la consulta de grupo revisa los de todas las reseñas.
        val existingIds = firestore.collectionGroup("comments").get(Source.SERVER).await().documents
            .mapNotNull { it.getLong("id")?.toInt() }
        return (existingIds.maxOrNull() ?: 0) + 1
    }
}
