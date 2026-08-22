package com.example.devicersapp.data.local

import com.example.devicersapp.R
import com.example.devicersapp.ui.models.ActivityContent
import com.example.devicersapp.ui.models.ActivityGroupContent
import com.example.devicersapp.ui.models.ActivityType

private const val MINUTE_IN_MILLIS = 60_000L
private const val HOUR_IN_MILLIS = 60 * MINUTE_IN_MILLIS
private const val DAY_IN_MILLIS = 24 * HOUR_IN_MILLIS

/** Provee los eventos de ejemplo que alimentan la pantalla de actividad, agrupados por periodo. */
object LocalActivityScreenProvider {

    fun activityGroups(currentTimeMillis: Long): List<ActivityGroupContent> = listOf(

        ActivityGroupContent(
            id = "today",
            titleResId = R.string.activity_group_today,
            activities = listOf(

                ActivityContent(
                    id = "today-camila",
                    type = ActivityType.LIKE,
                    avatarResId = R.drawable.profile_avatar_00,
                    authorResId = R.string.activity_author_camila,
                    actionResId = R.string.activity_action_liked_review,
                    detailResId = R.string.activity_detail_headphones,
                    time = currentTimeMillis - 5 * MINUTE_IN_MILLIS
                ),

                ActivityContent(
                    id = "today-david",
                    type = ActivityType.COMMENT,
                    avatarResId = R.drawable.profile_avatar_01,
                    authorResId = R.string.activity_author_david,
                    actionResId = R.string.activity_action_replied_comment,
                    detailResId = R.string.activity_detail_comment,
                    time = currentTimeMillis - HOUR_IN_MILLIS
                ),

                ActivityContent(
                    id = "today-mateo",
                    type = ActivityType.LIKE,
                    avatarResId = R.drawable.profile_avatar_02,
                    authorResId = R.string.activity_author_mateo,
                    actionResId = R.string.activity_action_liked_review,
                    detailResId = R.string.activity_detail_keyboard,
                    time = currentTimeMillis - 2 * HOUR_IN_MILLIS
                ),

                ActivityContent(
                    id = "today-lina",
                    type = ActivityType.FOLLOW,
                    avatarResId = R.drawable.profile_avatar_03,
                    authorResId = R.string.activity_author_lina,
                    actionResId = R.string.activity_action_followed,
                    detailResId = R.string.activity_detail_profile,
                    time = currentTimeMillis - 3 * HOUR_IN_MILLIS,
                    showFollowAction = true
                ),

                ActivityContent(
                    id = "today-camila-keyboard",
                    type = ActivityType.LIKE,
                    avatarResId = R.drawable.profile_avatar_04,
                    authorResId = R.string.activity_author_camila,
                    actionResId = R.string.activity_action_liked_review,
                    detailResId = R.string.activity_detail_keyboard,
                    time = currentTimeMillis - 4 * HOUR_IN_MILLIS
                ),

                ActivityContent(
                    id = "today-david-headphones",
                    type = ActivityType.COMMENT,
                    avatarResId = R.drawable.profile_avatar_05,
                    authorResId = R.string.activity_author_david,
                    actionResId = R.string.activity_action_replied_comment,
                    detailResId = R.string.activity_detail_headphones,
                    time = currentTimeMillis - 5 * HOUR_IN_MILLIS
                )
            )
        ),

        ActivityGroupContent(
            id = "yesterday",
            titleResId = R.string.activity_group_yesterday,
            activities = listOf(

                ActivityContent(
                    id = "yesterday-lina",
                    type = ActivityType.FOLLOW,
                    avatarResId = R.drawable.profile_avatar_09,
                    authorResId = R.string.activity_author_lina,
                    actionResId = R.string.activity_action_followed,
                    detailResId = R.string.activity_detail_profile,
                    time = currentTimeMillis - DAY_IN_MILLIS,
                    showFollowAction = true
                ),

                ActivityContent(
                    id = "yesterday-david",
                    type = ActivityType.LIKE,
                    avatarResId = R.drawable.profile_avatar_07,
                    authorResId = R.string.activity_author_david,
                    actionResId = R.string.activity_action_liked_review,
                    detailResId = R.string.activity_detail_keyboard,
                    time = currentTimeMillis - DAY_IN_MILLIS - HOUR_IN_MILLIS
                ),

                ActivityContent(
                    id = "yesterday-mateo",
                    type = ActivityType.COMMENT,
                    avatarResId = R.drawable.profile_avatar_04,
                    authorResId = R.string.activity_author_mateo,
                    actionResId = R.string.activity_action_replied_comment,
                    detailResId = R.string.activity_detail_comment,
                    time = currentTimeMillis - DAY_IN_MILLIS - 2 * HOUR_IN_MILLIS
                )
            )
        ),

        ActivityGroupContent(
            id = "earlier",
            titleResId = R.string.activity_group_earlier,
            activities = listOf(

                ActivityContent(
                    id = "earlier-camila",
                    type = ActivityType.LIKE,
                    avatarResId = R.drawable.profile_avatar_03,
                    authorResId = R.string.activity_author_camila,
                    actionResId = R.string.activity_action_liked_review,
                    detailResId = R.string.activity_detail_headphones,
                    time = currentTimeMillis - 2 * DAY_IN_MILLIS
                ),

                ActivityContent(
                    id = "earlier-lina-keyboard",
                    type = ActivityType.COMMENT,
                    avatarResId = R.drawable.profile_avatar_06,
                    authorResId = R.string.activity_author_lina,
                    actionResId = R.string.activity_action_replied_comment,
                    detailResId = R.string.activity_detail_keyboard,
                    time = currentTimeMillis - 4 * DAY_IN_MILLIS
                ),

                ActivityContent(
                    id = "earlier-mateo-follow",
                    type = ActivityType.FOLLOW,
                    avatarResId = R.drawable.profile_avatar_02,
                    authorResId = R.string.activity_author_mateo,
                    actionResId = R.string.activity_action_followed,
                    detailResId = R.string.activity_detail_profile,
                    time = currentTimeMillis - 6 * DAY_IN_MILLIS,
                    showFollowAction = true
                )
            )
        )
    )
}
