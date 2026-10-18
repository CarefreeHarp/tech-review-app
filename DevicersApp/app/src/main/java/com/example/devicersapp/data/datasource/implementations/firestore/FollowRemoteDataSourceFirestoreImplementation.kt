package com.example.devicersapp.data.datasource.implementations.firestore
import com.example.devicersapp.data.datasource.*
import com.example.devicersapp.ui.models.*
import com.example.devicersapp.data.dto.ResponseDTO.firestore.*
import com.example.devicersapp.data.dto.ResponseDTO.shared.*
import com.example.devicersapp.data.dto.RequestDTO.firestore.*
import com.google.firebase.firestore.*
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/** Implementa las operaciones de seguimientos con las rutas y campos de Firestore v9. */
class FollowRemoteDataSourceFirestoreImplementation @Inject constructor(
    private val firestore: FirebaseFirestore
) : FollowRemoteDataSource {
    /** Guarda los dos sentidos, sus contadores y la notificación en una sola transacción. */
    override suspend fun setFollowing(followerId: Int, followedId: Int, following: Boolean): FollowStatus {
        require(followerId > 0 && followedId > 0 && followerId != followedId)
        val users = firestore.collection("users")
        val actorRef = users.document(followerId.toString())
        val targetRef = users.document(followedId.toString())
        val followRef = actorRef.collection("follows").document(followedId.toString())
        val followerRef = targetRef.collection("followers").document(followerId.toString())
        val notifications = firestore.collection("notifications")
        val sequence = firestore.collection("counters").document("next_id")
        // El SDK cliente consulta fuera de la transacción, igual que en los likes de reseñas.
        val previous = notifications.whereEqualTo("user_id", followedId).get(Source.SERVER).await()
            .documents.filter { it.getString("type") == NotificationTypes.FOLLOW && it.getLong("actor_user_id") == followerId.toLong() }
        val seed = if (following) {
            sequence.get(Source.SERVER).await().getLong("next_notification_id")?.toInt()
                ?: ((notifications.orderBy("id", Query.Direction.DESCENDING).limit(1).get(Source.SERVER).await()
                    .documents.firstOrNull()?.integer("id") ?: 0) + 1)
        } else 0
        return firestore.runTransaction { transaction ->
            val actor = transaction.get(actorRef)
            val target = transaction.get(targetRef)
            check(actor.exists() && target.exists()) { "No existe uno de los perfiles." }
            val existingFollow = transaction.get(followRef)
            val followExists = existingFollow.exists()
            val followerExists = transaction.get(followerRef).exists()
            val notificationId = if (following && !followExists) {
                var next = transaction.get(sequence).getLong("next_notification_id")?.toInt() ?: seed
                check(next > 0)
                while (transaction.get(notifications.document(next.toString())).exists()) {
                    check(next < Int.MAX_VALUE) { "Se agotaron los IDs de notificación." }
                    next++
                }
                next
            } else null
            val followDelta = (if (following) 1 else 0) - (if (followExists) 1 else 0)
            val followerDelta = (if (following) 1 else 0) - (if (followerExists) 1 else 0)
            val count = ((target.getLong("followers_count")?.toInt() ?: 0) + followerDelta).coerceAtLeast(0)
            // Todas las lecturas terminan antes de escribir. Repetir el mismo estado no duplica relaciones.
            if (following) {
                if (!followExists) transaction.set(followRef, mapOf("followed_id" to followedId, "created_at" to FieldValue.serverTimestamp(), "notification_id" to notificationId))
                if (!followerExists) transaction.set(followerRef, mapOf("follower_id" to followerId, "created_at" to FieldValue.serverTimestamp()))
            } else {
                transaction.delete(followRef)
                transaction.delete(followerRef)
                existingFollow.getLong("notification_id")?.let { transaction.delete(notifications.document(it.toString())) }
            }
            if (followDelta != 0) transaction.update(actorRef, "follows_count", ((actor.getLong("follows_count")?.toInt() ?: 0) + followDelta).coerceAtLeast(0))
            if (followerDelta != 0) transaction.update(targetRef, "followers_count", count)
            if (!following || notificationId != null) previous.forEach { transaction.delete(it.reference) }
            notificationId?.let { id ->
                transaction.set(notifications.document(id.toString()), CreateNotificationFirestoreDto(
                    user_id = followedId, actor_user_id = followerId, type = NotificationTypes.FOLLOW,
                    review_id = null, comment_id = null).fields(id))
                transaction.set(sequence, mapOf("next_notification_id" to id.toLong() + 1), SetOptions.merge())
            }
            FollowStatus(following, count)
        }.await()
    }

    override suspend fun getFollowers(userId: Int): List<FollowInfo> {
        require(userId > 0)
        return firestore.collection("users").document(userId.toString()).collection("followers").get(Source.SERVER).await().documents
            .map { it.toFollowerFirestoreDto().toFollowInfo(userId) }
    }
    override suspend fun getFollowing(userId: Int): List<FollowInfo> {
        require(userId > 0)
        return firestore.collection("users").document(userId.toString()).collection("follows").get(Source.SERVER).await().documents
            .map { it.toFollowFirestoreDto().toFollowInfo(userId) }
    }
}
