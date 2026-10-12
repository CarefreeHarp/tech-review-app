package com.example.devicersapp

import androidx.lifecycle.ViewModelStore
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.devicersapp.data.datasource.ProductSuggestion
import com.example.devicersapp.data.datasource.implementations.*
import com.example.devicersapp.data.repository.*
import com.example.devicersapp.ui.screens.activity.ActivityViewModel
import com.example.devicersapp.ui.screens.found_products.FoundProductsViewModel
import com.example.devicersapp.ui.screens.profile_search_results.ProfileSearchResultsViewModel
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import org.junit.Assert.*
import org.junit.Assume.assumeTrue
import org.junit.Test
import org.junit.runner.RunWith
import java.util.UUID

/** Opt-in: credenciales de una cuenta de prueba vía argumentos del runner, nunca en el repositorio. */
@RunWith(AndroidJUnit4::class)
class DiscoveryFirestoreFlowTest {
    @Test fun firestoreReadsAndProductSuggestionRoundTrip() = runBlocking {
        val args = InstrumentationRegistry.getArguments()
        val email = args.getString("discoveryTestEmail")
        val password = args.getString("discoveryTestPassword")
        assumeTrue(!email.isNullOrBlank() && !password.isNullOrBlank())
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val app = FirebaseApp.initializeApp(context, FirebaseApp.getInstance().options, "discovery-${UUID.randomUUID()}")
        val auth = FirebaseAuth.getInstance(app)
        val firestore = FirebaseFirestore.getInstance(app)
        val store = ViewModelStore()
        val requestId = "discovery-test-${UUID.randomUUID()}"
        var wroteRequest = false
        try {
            withTimeout(90_000) {
                val account = auth.signInWithEmailAndPassword(email!!, password!!).await().user!!
                val profiles = UserProfileFirestoreDataSourceImplementation(firestore)
                // Solo una cuenta ya provisionada: no crea ni modifica perfiles de prueba.
                val profile = checkNotNull(profiles.findByFirebaseUid(account.uid))
                val session = SessionRepository(profiles)
                session.loadProfile(account.uid, profile.email, profile.username, profile.profileImageUrl)
                val source = DiscoveryFirestoreDataSource(firestore)
                val repository = DiscoveryRepository(source)
                withContext(Dispatchers.Main) {
                    val products = FoundProductsViewModel(repository).also { store.put("products", it) }
                    products.loadProducts("", "all", 0f, "rated")
                    val catalog = products.uiState.first { !it.isLoading }
                    assertNull(catalog.errorMessage)
                    assertTrue(catalog.results.isNotEmpty())
                    assertTrue(catalog.results.all { it.id > 0 && it.isActive })
                    val search = ProfileSearchResultsViewModel(repository, session).also { store.put("profiles", it) }
                    val found = search.uiState.first { !it.loading }
                    assertNull(found.error)
                    assertTrue(found.results.isNotEmpty())
                    assertTrue(found.results.none { it.id == profile.id.toString() })
                    val activity = ActivityViewModel(repository, session).also { store.put("activity", it) }
                    val feed = activity.uiState.first { !it.loading }
                    assertNull(feed.error)
                    assertTrue(feed.activityGroups.isNotEmpty())
                    assertTrue(feed.activityGroups.flatMap { it.notifications }.all {
                        it.actorProfileId.toInt() in feed.actorNames
                    })
                }
                val requests = ProductRequestsRepository(ProductRequestsFirestoreDataSource(firestore, auth), session)
                val suggestion = ProductSuggestion("TEST automatizado tareas 12-15", "Prueba", "Prueba", "")
                requests.submit(requestId, suggestion)
                wroteRequest = true
                val first = firestore.collection("product_requests").document(requestId).get(Source.SERVER).await()
                assertEquals(profile.id.toLong(), first.getLong("user_id"))
                assertEquals(account.uid, first.getString("firebase_uid"))
                assertEquals(suggestion.productName, first.getString("product_name"))
                assertEquals("pending", first.getString("status"))
                assertNotNull(first.getTimestamp("createdAt"))
                requests.submit(requestId, suggestion)
                val retried = firestore.collection("product_requests").document(requestId).get(Source.SERVER).await()
                assertEquals(first.getTimestamp("createdAt"), retried.getTimestamp("createdAt"))
            }
        } finally {
            withContext(Dispatchers.Main) { store.clear() }
            if (wroteRequest) {
                val ref = firestore.collection("product_requests").document(requestId)
                ref.delete().await()
                assertFalse(ref.get(Source.SERVER).await().exists())
            }
            auth.signOut()
            firestore.terminate().await()
            app.delete()
        }
    }
}
