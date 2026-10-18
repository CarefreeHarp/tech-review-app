package com.example.devicersapp.data.dto.ResponseDTO.retrofit
import com.example.devicersapp.ui.models.FollowStatus
/** Respuesta confirmada por la transacción de seguimiento de Express. */
data class FollowStatusRetrofitDto(val is_following: Boolean, val followers_count: Int)
/** Conserva el contrato común usado por la versión Firestore. */
fun FollowStatusRetrofitDto.toFollowStatus() = FollowStatus(is_following, followers_count)
