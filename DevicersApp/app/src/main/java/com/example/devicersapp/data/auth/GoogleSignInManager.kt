package com.example.devicersapp.data.auth

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import com.example.devicersapp.R
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import javax.inject.Inject

/** Abre el selector nativo de cuentas y extrae el ID token devuelto por Google. */
class GoogleSignInManager @Inject constructor() {
    suspend fun getIdToken(context: Context): String? {
        val googleOption = GetSignInWithGoogleOption.Builder(
            context.getString(R.string.default_web_client_id)
        ).build()
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleOption)
            .build()
        val response = try {
            CredentialManager.create(context).getCredential(context, request)
        } catch (_: GetCredentialCancellationException) {
            return null
        }
        val credential = response.credential
        check(
            credential is CustomCredential &&
                credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
        ) { "Google devolvió un tipo de credencial no compatible." }
        return GoogleIdTokenCredential.createFrom(credential.data).idToken
    }
}
