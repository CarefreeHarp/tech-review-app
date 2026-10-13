package com.example.devicersapp

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.devicersapp.ui.models.*
import com.example.devicersapp.ui.screens.activity.*
import com.example.devicersapp.ui.theme.DevicersAppTheme
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DiscoveryUiTest {
    @get:Rule val compose = createComposeRule()

    @Test fun activityRendersRemoteActorAndEmitsSelectedReviewId() {
        var selected: Int? = null
        val event = ActivityContent("remote-event", ActivityType.COMMENT, "98765",
            R.string.discovery_activity_commented, targetReviewId = 76543, time = System.currentTimeMillis())
        compose.setContent {
            DevicersAppTheme {
                ActivityViewContent(
                    state = ActivityState(activityGroups = listOf(ActivityGroupContent("today", R.string.activity_group_today, listOf(event))),
                        actorNames = mapOf(98765 to "Autora remota"), productNames = mapOf(76543 to "Producto remoto")),
                    onFollow = {}, onActivityClick = { selected = it.targetReviewId },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
        compose.onNodeWithText("@Autora remota").assertIsDisplayed()
        compose.onNodeWithText("Producto remoto", substring = true, useUnmergedTree = true).performClick()
        compose.runOnIdle { assertEquals(76543, selected) }
    }

    @Test fun activityFailureOffersRetry() {
        var retried = false
        compose.setContent {
            DevicersAppTheme {
                ActivityViewContent(ActivityState(error = "Sin conexión"), {}, {}, onRetry = { retried = true })
            }
        }
        compose.onNodeWithText("Sin conexión").assertIsDisplayed()
        compose.onNodeWithText("Reintentar").performClick()
        compose.runOnIdle { assertTrue(retried) }
    }
}
