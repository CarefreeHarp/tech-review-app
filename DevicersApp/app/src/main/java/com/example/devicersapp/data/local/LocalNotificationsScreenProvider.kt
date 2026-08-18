package com.example.devicersapp.data.local

import com.example.devicersapp.R
import com.example.devicersapp.ui.models.NotificationContent
import com.example.devicersapp.ui.models.NotificationGroupContent

private const val MINUTE_IN_MILLIS = 60_000L
private const val HOUR_IN_MILLIS = 60 * MINUTE_IN_MILLIS
private const val DAY_IN_MILLIS = 24 * HOUR_IN_MILLIS

object LocalNotificationsScreenProvider {

    fun notificationGroups(currentTimeMillis: Long): List<NotificationGroupContent> = listOf(

        NotificationGroupContent(
            id = "today",
            titleResId = R.string.notifications_group_today,
            notifications = listOf(

                NotificationContent(
                    id = "today-camila",
                    avatarResId = R.drawable.profile_avatar_00,
                    authorResId = R.string.notification_author_camila,
                    actionResId = R.string.notification_action_liked_review,
                    detailResId = R.string.notification_detail_headphones,
                    time = currentTimeMillis - 5 * MINUTE_IN_MILLIS
                ),

                NotificationContent(
                    id = "today-david",
                    avatarResId = R.drawable.profile_avatar_01,
                    authorResId = R.string.notification_author_david,
                    actionResId = R.string.notification_action_replied_comment,
                    detailResId = R.string.notification_detail_comment,
                    time = currentTimeMillis - HOUR_IN_MILLIS
                ),

                NotificationContent(
                    id = "today-mateo",
                    avatarResId = R.drawable.profile_avatar_02,
                    authorResId = R.string.notification_author_mateo,
                    actionResId = R.string.notification_action_liked_review,
                    detailResId = R.string.notification_detail_keyboard,
                    time = currentTimeMillis - 2 * HOUR_IN_MILLIS
                ),

                NotificationContent(
                    id = "today-lina",
                    avatarResId = R.drawable.profile_avatar_03,
                    authorResId = R.string.notification_author_lina,
                    actionResId = R.string.notification_action_followed,
                    detailResId = R.string.notification_detail_profile,
                    time = currentTimeMillis - 3 * HOUR_IN_MILLIS,
                    showFollowAction = true
                ),

                NotificationContent(
                    id = "today-camila-keyboard",
                    avatarResId = R.drawable.profile_avatar_04,
                    authorResId = R.string.notification_author_camila,
                    actionResId = R.string.notification_action_liked_review,
                    detailResId = R.string.notification_detail_keyboard,
                    time = currentTimeMillis - 4 * HOUR_IN_MILLIS
                ),

                NotificationContent(
                    id = "today-david-headphones",
                    avatarResId = R.drawable.profile_avatar_05,
                    authorResId = R.string.notification_author_david,
                    actionResId = R.string.notification_action_replied_comment,
                    detailResId = R.string.notification_detail_headphones,
                    time = currentTimeMillis - 5 * HOUR_IN_MILLIS
                ),

                NotificationContent(
                    id = "today-mateo-comment",
                    avatarResId = R.drawable.profile_avatar_06,
                    authorResId = R.string.notification_author_mateo,
                    actionResId = R.string.notification_action_replied_comment,
                    detailResId = R.string.notification_detail_comment,
                    time = currentTimeMillis - 6 * HOUR_IN_MILLIS
                ),

                NotificationContent(
                    id = "today-lina-headphones",
                    avatarResId = R.drawable.profile_avatar_07,
                    authorResId = R.string.notification_author_lina,
                    actionResId = R.string.notification_action_liked_review,
                    detailResId = R.string.notification_detail_headphones,
                    time = currentTimeMillis - 7 * HOUR_IN_MILLIS
                ),

                NotificationContent(
                    id = "today-camila-follow",
                    avatarResId = R.drawable.profile_avatar_08,
                    authorResId = R.string.notification_author_camila,
                    actionResId = R.string.notification_action_followed,
                    detailResId = R.string.notification_detail_profile,
                    time = currentTimeMillis - 8 * HOUR_IN_MILLIS,
                    showFollowAction = true
                )
            )
        ),

        NotificationGroupContent(
            id = "yesterday",
            titleResId = R.string.notifications_group_yesterday,
            notifications = listOf(

                NotificationContent(
                    id = "yesterday-lina",
                    avatarResId = R.drawable.profile_avatar_09,
                    authorResId = R.string.notification_author_lina,
                    actionResId = R.string.notification_action_followed,
                    detailResId = R.string.notification_detail_profile,
                    time = currentTimeMillis - DAY_IN_MILLIS,
                    showFollowAction = true
                ),

                NotificationContent(
                    id = "yesterday-david",
                    avatarResId = R.drawable.profile_avatar_07,
                    authorResId = R.string.notification_author_david,
                    actionResId = R.string.notification_action_liked_review,
                    detailResId = R.string.notification_detail_keyboard,
                    time = currentTimeMillis - DAY_IN_MILLIS - HOUR_IN_MILLIS
                ),

                NotificationContent(
                    id = "yesterday-mateo",
                    avatarResId = R.drawable.profile_avatar_04,
                    authorResId = R.string.notification_author_mateo,
                    actionResId = R.string.notification_action_replied_comment,
                    detailResId = R.string.notification_detail_comment,
                    time = currentTimeMillis - DAY_IN_MILLIS - 2 * HOUR_IN_MILLIS
                ),

                NotificationContent(
                    id = "yesterday-camila",
                    avatarResId = R.drawable.profile_avatar_03,
                    authorResId = R.string.notification_author_camila,
                    actionResId = R.string.notification_action_liked_review,
                    detailResId = R.string.notification_detail_headphones,
                    time = currentTimeMillis - DAY_IN_MILLIS - 3 * HOUR_IN_MILLIS
                ),

                NotificationContent(
                    id = "yesterday-lina-keyboard",
                    avatarResId = R.drawable.profile_avatar_06,
                    authorResId = R.string.notification_author_lina,
                    actionResId = R.string.notification_action_liked_review,
                    detailResId = R.string.notification_detail_keyboard,
                    time = currentTimeMillis - DAY_IN_MILLIS - 4 * HOUR_IN_MILLIS
                )
            )
        )
    )
}