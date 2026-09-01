package com.example.devicersapp.data.local

import com.example.devicersapp.R
import com.example.devicersapp.ui.models.ActivityContent
import com.example.devicersapp.ui.models.ActivityGroupContent
import com.example.devicersapp.ui.models.ActivityType

private const val MINUTE_IN_MILLIS = 60_000L
private const val HOUR_IN_MILLIS = 60 * MINUTE_IN_MILLIS
private const val DAY_IN_MILLIS = 24 * HOUR_IN_MILLIS

/** Provee eventos que siempre apuntan a perfiles o reseñas existentes. */
object LocalActivityProvider {

    fun activityGroups(currentTimeMillis: Long): List<ActivityGroupContent> {
        val groups = listOf(
        ActivityGroupContent(
            id = "today",
            titleResId = R.string.activity_group_today,
            notifications = listOf(
                reviewActivity("today-camila", ActivityType.LIKE, "camila", R.string.activity_action_liked_review, R.string.feed_product_audio, currentTimeMillis - 5 * MINUTE_IN_MILLIS),
                reviewActivity("today-david", ActivityType.COMMENT, "mariana", R.string.activity_action_replied_comment, R.string.feed_product_phone, currentTimeMillis - HOUR_IN_MILLIS),
                reviewActivity("today-mateo", ActivityType.LIKE, "audio_fan", R.string.activity_action_liked_review, R.string.feed_product_nine, currentTimeMillis - 2 * HOUR_IN_MILLIS),
                followActivity("today-lina", "nora_mobile", currentTimeMillis - 3 * HOUR_IN_MILLIS),
                reviewActivity("today-camila-keyboard", ActivityType.LIKE, "diego_gadgets", R.string.activity_action_liked_review, R.string.feed_product_five, currentTimeMillis - 4 * HOUR_IN_MILLIS),
                reviewActivity("today-david-headphones", ActivityType.COMMENT, "luna_digital", R.string.activity_action_replied_comment, R.string.review_card_username, currentTimeMillis - 5 * HOUR_IN_MILLIS)
            )
        ),
        ActivityGroupContent(
            id = "yesterday",
            titleResId = R.string.activity_group_yesterday,
            notifications = listOf(
                followActivity("yesterday-lina", "camila", currentTimeMillis - DAY_IN_MILLIS),
                reviewActivity("yesterday-david", ActivityType.LIKE, "mariana", R.string.activity_action_liked_review, R.string.feed_product_seven, currentTimeMillis - DAY_IN_MILLIS - HOUR_IN_MILLIS),
                reviewActivity("yesterday-mateo", ActivityType.COMMENT, "audio_fan", R.string.activity_action_replied_comment, R.string.feed_product_computer, currentTimeMillis - DAY_IN_MILLIS - 2 * HOUR_IN_MILLIS)
            )
        ),
        ActivityGroupContent(
            id = "earlier",
            titleResId = R.string.activity_group_earlier,
            notifications = listOf(
                reviewActivity("earlier-camila", ActivityType.LIKE, "diego_gadgets", R.string.activity_action_liked_review, R.string.feed_product_eleven, currentTimeMillis - 2 * DAY_IN_MILLIS),
                reviewActivity("earlier-lina-keyboard", ActivityType.COMMENT, "nora_mobile", R.string.activity_action_replied_comment, R.string.feed_product_ten, currentTimeMillis - 4 * DAY_IN_MILLIS),
                followActivity("earlier-mateo-follow", "luna_digital", currentTimeMillis - 6 * DAY_IN_MILLIS)
            )
        )
        )

        // La actividad local no admite destinos huérfanos: toda interacción debe poder abrirse.
        groups.flatMap(ActivityGroupContent::notifications).forEach { activity ->
            when (activity.type) {
                ActivityType.LIKE, ActivityType.COMMENT -> {
                    require(LocalReviewProvider.findById(requireNotNull(activity.targetReviewId)) != null)
                }
                ActivityType.FOLLOW -> {
                    require(LocalProfileProvider.getProfileById(requireNotNull(activity.targetProfileId)) != null)
                }
            }
        }
        return groups
    }

    /** Crea una notificación vinculada a una reseña real. */
    private fun reviewActivity(id: String, type: ActivityType, actorProfileId: String, actionResId: Int, reviewId: Int, time: Long) =
        ActivityContent(id, type, actorProfileId, actionResId, targetReviewId = reviewId, time = time)

    /** Crea una notificación de seguimiento vinculada al perfil que la originó. */
    private fun followActivity(id: String, profileId: String, time: Long) =
        ActivityContent(
            id = id,
            type = ActivityType.FOLLOW,
            actorProfileId = profileId,
            actionResId = R.string.activity_action_followed,
            targetProfileId = profileId,
            time = time,
            showFollowAction = true
        )
}
