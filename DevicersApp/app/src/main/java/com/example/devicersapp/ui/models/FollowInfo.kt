package com.example.devicersapp.ui.models
/** Relación dirigida; follows y followers son las dos vistas del mismo vínculo. */
data class FollowInfo(val followerId: Int, val followedId: Int, val createdAt: String?)
