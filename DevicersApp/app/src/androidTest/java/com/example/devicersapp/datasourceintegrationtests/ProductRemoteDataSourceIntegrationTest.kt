package com.example.devicersapp.datasourceintegrationtests

import com.example.devicersapp.data.datasource.ProductRemoteDataSource
import com.example.devicersapp.data.datasource.implementations.firestore.ProductRemoteDataSourceFirestoreImplementation
import com.google.common.truth.Truth
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

/** Pruebas de integración de [ProductRemoteDataSource] contra Firestore del emulador local. */
class ProductRemoteDataSourceIntegrationTest {
    private val db = Firebase.firestore
    private val auth = Firebase.auth
    private lateinit var productDataSource: ProductRemoteDataSourceFirestoreImplementation

    /** Configura el destino local, prepara la sesión y carga los datos previos a cada prueba. */
    @Before
    fun setUp() = runTest {
        // En el emulador de Android, 10.0.2.2 permite acceder a Firebase en el computador anfitrión.
        try {
            db.useEmulator("10.0.2.2", 8080)
            auth.useEmulator("10.0.2.2", 9099)
        } catch (e: Exception) {
            // La comprobación siguiente verifica el destino efectivo aunque falle la reconfiguración.
        }
        // Esta validación detiene la preparación si Firestore no quedó conectado al emulador local.
        Truth.assertThat(db.firestoreSettings.host).isEqualTo("10.0.2.2:8080")

        // Crea una sesión anónima solo si Firebase Auth no conserva una sesión previa.
        if (auth.currentUser == null) {
            auth.signInAnonymously().await()
        }

        productDataSource = ProductRemoteDataSourceFirestoreImplementation(db)

        // Crea tres artículos conocidos (1000 a 1002) antes de cada prueba.
        val batch = db.batch()
        repeat(3) { i ->
            val product = generateProduct(i)
            batch.set(db.collection("articles").document(product["id"].toString()), product)
        }

        // El artículo 1001 recibe una tienda para verificar la lectura de la relación.
        val storeData = mapOf(
            "store_id" to 1,
            "name" to "Tech Store Test",
            "website_url" to "https://test.com",
            "product_url" to "https://test.com/product/1001"
        )
        batch.set(db.collection("articles").document("1001").collection("stores").document("1"), storeData)

        batch.commit().await()
    }

    @Test
    fun getProductById_existingProduct_returnsProductWithStores() = runTest {
        // Preparación (Arrange): Selecciona el artículo 1001, que tiene una tienda asociada en setUp.
        val targetId = 1001
        // Ejecución (Act): Consulta el artículo con sus tiendas.
        val result = productDataSource.getProductById(targetId)

        // Verificación (Assert): Verifica los campos del producto y la tienda incorporada al resultado.
        Truth.assertThat(result.id).isEqualTo(targetId)
        Truth.assertThat(result.name).isEqualTo("Producto 1")
        Truth.assertThat(result.stores).hasSize(1)
        Truth.assertThat(result.stores.first().name).isEqualTo("Tech Store Test")
    }

    @Test
    fun getProductById_nonExistingProduct_throwsNoSuchElementException() = runTest {
        // Preparación (Arrange): Usa un ID que no se inserta durante la preparación.
        val invalidId = 9999
        // Ejecución (Act): Captura la excepción de la consulta para poder comprobar su tipo.
        val result = runCatching { productDataSource.getProductById(invalidId) }

        // Verificación (Assert): Comprueba que la ausencia se comunique con NoSuchElementException.
        Truth.assertThat(result.isFailure).isTrue()
        Truth.assertThat(result.exceptionOrNull()).isInstanceOf(NoSuchElementException::class.java)
    }

    @Test
    fun getProducts_fetchesAllDocumentsSuccessfully() = runTest {
        // Preparación (Arrange): setUp inserta los artículos 1000, 1001 y 1002.
        // Ejecución (Act): Consulta el catálogo completo.
        val results = productDataSource.getProducts()
        val resultIds = results.map { it.id }
        // Verificación (Assert): Compara los IDs; permite que la colección contenga otros artículos.
        Truth.assertThat(resultIds).containsAtLeast(1000, 1001, 1002)
    }

    @Test
    fun getStores_productWithStores_returnsStoreList() = runTest {
        // Preparación (Arrange): Selecciona el producto con una tienda creada en setUp.
        val targetProductId = 1001
        // Ejecución (Act): Consulta únicamente su subcolección de tiendas.
        val stores = productDataSource.getStores(targetProductId)

        // Verificación (Assert): Verifica la identidad y el nombre de la tienda devuelta.
        Truth.assertThat(stores).isNotEmpty()
        Truth.assertThat(stores.first().storeId).isEqualTo(1)
        Truth.assertThat(stores.first().name).isEqualTo("Tech Store Test")
    }

    /** Ejecuta la limpieza posterior a cada caso con el alcance definido por esta clase. */
    @After
    fun tearDown() = runTest {
        // JUnit ejecuta @After aunque falle @Before; nunca limpiar un destino distinto al emulador.
        if (db.firestoreSettings.host != "10.0.2.2:8080") {
            return@runTest
        }

        // La limpieza actual elimina todos los artículos y sus tiendas, incluidos datos ajenos a setUp.
        val articles = db.collection("articles").get().await()
        for (articleDoc in articles) {
            // Las tiendas deben borrarse antes que el documento del artículo.
            val stores = articleDoc.reference.collection("stores").get().await()
            for (store in stores) {
                store.reference.delete().await()
            }
            articleDoc.reference.delete().await()
        }
    }
}