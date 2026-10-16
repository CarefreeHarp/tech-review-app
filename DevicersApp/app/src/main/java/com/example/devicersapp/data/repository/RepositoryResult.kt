package com.example.devicersapp.data.repository
import kotlinx.coroutines.CancellationException
/** Conserva la causa original de los errores sin interceptar la cancelación de corutinas. */
internal suspend fun <T> repositoryResult(operation: suspend () -> T): Result<T> = try {
    Result.success(operation())
} catch (cancelled: CancellationException) { throw cancelled
} catch (error: Exception) { Result.failure(error) }
