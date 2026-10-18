package com.example.devicersapp.ui.models
/** Notificación persistida; conserva el tipo original y las referencias opcionales. */
data class NotificationInfo(val id: String, val userId: Int, val actorUserId: Int, val type: String,
    val reviewId: Int?, val commentId: Int?, val isRead: Boolean, val createdAt: String)

/** Valores persistidos en el campo `type` de una notificación. */
object NotificationTypes {
    const val REVIEW_LIKE = "review_like"
    const val COMMENT_LIKE = "comment_like"
    const val COMMENT = "comment"
    const val FOLLOW = "follow"
}
