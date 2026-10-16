package com.example.devicersapp.data.dto.ResponseDTO.retrofit
import com.example.devicersapp.ui.models.*
import com.example.devicersapp.data.dto.ResponseDTO.shared.*
/** Respuesta de Express para seguimientos; las asociaciones anidadas son opcionales según el endpoint. */
data class FollowRetrofitDto(
    val followed_id: Int,
    val created_at: String? = null,
    val follower_id: Int,
    val createdAt: String? = null
)
/** Conserva las fechas canónicas de SQL y usa los alias solo cuando el endpoint no las devuelve. */
fun FollowRetrofitDto.toFollowInfo(): FollowInfo = FollowInfo(
    followedId = followed_id,
    createdAt = created_at ?: createdAt,
    followerId = follower_id
)
