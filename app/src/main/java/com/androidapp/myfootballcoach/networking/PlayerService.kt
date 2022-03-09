package com.androidapp.myfootballcoach.networking

import retrofit2.http.GET

interface PlayerService {

    @GET("seed=empatica&results=5000")
    suspend fun loadPlayers(): PlayerDTO
}
