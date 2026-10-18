package com.example.devicersapp.data.dto.RequestDTO.retrofit
/** Estado deseado del seguimiento; repetirlo no vuelve a alternar la relación. */
data class SetFollowingRequestDto(val follower_id: Int, val is_following: Boolean)
