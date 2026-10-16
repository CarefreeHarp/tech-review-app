package com.example.devicersapp.data.injection
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
/** Proporciona exclusivamente las instancias compartidas de los SDK de Firebase. */
@Module @InstallIn(SingletonComponent::class)
object FirebaseHiltModule {
    @Provides @Singleton fun provideAuth(): FirebaseAuth = Firebase.auth
    @Provides @Singleton fun provideFirestore(): FirebaseFirestore = Firebase.firestore
    @Provides @Singleton fun provideStorage(): FirebaseStorage = Firebase.storage
}
