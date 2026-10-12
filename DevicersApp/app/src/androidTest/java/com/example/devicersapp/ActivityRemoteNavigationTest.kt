package com.example.devicersapp

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.devicersapp.data.datasource.implementations.DiscoveryFirestoreDataSource
import com.example.devicersapp.data.datasource.implementations.UserProfileFirestoreDataSourceImplementation
import com.example.devicersapp.data.local.LocalReviewProvider
import com.example.devicersapp.data.repository.DiscoveryRepository
import com.example.devicersapp.data.repository.SessionRepository
import com.example.devicersapp.ui.models.ActivityType
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.Assert.*
import org.junit.Assume.assumeTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.junit.runners.model.Statement

/** Ejecutar sobre la combinación con tasks/#5-6-7: grafo y destino reales, solo lecturas. */
@RunWith(AndroidJUnit4::class)
class ActivityRemoteNavigationTest {
    private val compose = createAndroidComposeRule<MainActivity>()
    private lateinit var expectedBody: String
    private lateinit var productName: String

    // Prepara identidad y datos antes de que MainActivity cree el grafo y sus ViewModels.
    private val identity = TestRule { statement, _ ->
        object : Statement() {
            override fun evaluate() {
                val args = InstrumentationRegistry.getArguments()
                val email = args.getString("discoveryTestEmail")
                val password = args.getString("discoveryTestPassword")
                assumeTrue(!email.isNullOrBlank() && !password.isNullOrBlank())
                assumeTrue(args.getString("discoveryRemoteReview") == "true")
                assumeTrue(BuildConfig.DEVICERS_TEST_START_DESTINATION == "activity")
                val context = InstrumentationRegistry.getInstrumentation().targetContext
                val session = EntryPointAccessors.fromApplication(context, ActivityNavigationSessionEntryPoint::class.java).session()
                val auth = FirebaseAuth.getInstance()
                val firestore = FirebaseFirestore.getInstance()
                try {
                    runBlocking {
                        val account = auth.signInWithEmailAndPassword(email!!, password!!).await().user!!
                        val profiles = UserProfileFirestoreDataSourceImplementation(firestore)
                        val profile = checkNotNull(profiles.findByFirebaseUid(account.uid))
                        session.loadProfile(account.uid, profile.email, profile.username, profile.profileImageUrl)
                        val source = DiscoveryFirestoreDataSource(firestore)
                        val feed = DiscoveryRepository(source).getActivity(profile.id)
                        val event = feed.groups.flatMap { it.notifications }.first { it.type != ActivityType.FOLLOW }
                        val reviewId = checkNotNull(event.targetReviewId)
                        // La prueba falla si su dato no permite detectar una ruta local equivocada.
                        assertNull(LocalReviewProvider.findById(reviewId))
                        val review = source.getActiveReviews().single { it.id == reviewId }
                        expectedBody = review.body
                        productName = checkNotNull(feed.productNames[reviewId])
                        assertTrue(expectedBody.isNotBlank())
                    }
                    statement.evaluate()
                } finally {
                    auth.signOut()
                    session.clear()
                }
            }
        }
    }

    @get:Rule val rules: RuleChain = RuleChain.outerRule(identity).around(compose)

    @Test fun notificationOpensFirestoreReviewThroughProductionGraph() {
        compose.waitUntil(30_000) {
            compose.onAllNodesWithText(productName, substring = true, useUnmergedTree = true).fetchSemanticsNodes().isNotEmpty()
        }
        compose.onAllNodesWithText(productName, substring = true, useUnmergedTree = true).onFirst().performScrollTo().performClick()
        compose.waitUntil(30_000) {
            compose.onAllNodesWithContentDescription("Cargando").fetchSemanticsNodes().isEmpty() &&
                compose.onAllNodes(hasScrollToIndexAction()).fetchSemanticsNodes().isNotEmpty()
        }
        compose.onNode(hasScrollToIndexAction()).performScrollToNode(hasText(expectedBody))
        compose.onNodeWithText(expectedBody).assertIsDisplayed()
        compose.onNodeWithText("No se encontró la reseña.").assertDoesNotExist()
    }
}
