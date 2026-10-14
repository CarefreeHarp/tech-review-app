package com.example.devicersapp.data.datasource.implementations

import com.example.devicersapp.data.datasource.FollowListRemoteDataSource
import com.example.devicersapp.ui.models.FollowListUser
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/** Consulta follows por el perfil indicado y recupera únicamente los usuarios relacionados. */
class FollowListFirestoreDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) : FollowListRemoteDataSource {
    override suspend fun getFollowers(profileId: Int) = getRelatedUsers(profileId, "followed_id", "follower_id")
    override suspend fun getFollowing(profileId: Int) = getRelatedUsers(profileId, "follower_id", "followed_id")

    private suspend fun getRelatedUsers(profileId: Int, ownerField: String, relatedField: String): List<FollowListUser> {
        require(profileId > 0)
        val relations = firestore.collection("follows").whereEqualTo(ownerField, profileId)
            .get(Source.SERVER).await().documents
        val ids = relations.mapNotNull { it.getLong(relatedField) }.map(Math::toIntExact)
            .filter { it > 0 }.distinct()
        return coroutineScope {
            // Los lotes evitan una lectura individual por usuario y admiten listas de cualquier tamaño.
            ids.chunked(10).map { batch ->
                async {
                    firestore.collection("users").whereIn(FieldPath.documentId(), batch.map(Int::toString))
                        .get(Source.SERVER).await().documents.filter { it.getBoolean("is_active") == true }.map { document ->
                            val id = Math.toIntExact(checkNotNull(document.getLong("id")))
                            check(document.id == id.toString() && id in batch)
                            FollowListUser(id, document.getString("username").orEmpty(),
                                document.getString("biography").orEmpty(),
                                document.getString("profile_image_url")?.trim()?.takeIf { it.isNotEmpty() })
                        }
                }
            }.awaitAll().flatten()
        }
    }
}
