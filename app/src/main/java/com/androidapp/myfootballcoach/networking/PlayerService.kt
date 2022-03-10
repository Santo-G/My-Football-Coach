package com.androidapp.myfootballcoach.networking

import com.androidapp.myfootballcoach.networking.api.PlayerDTO
import retrofit2.http.GET

interface PlayerService {

    @GET("seed=empatica&inc=name,picture&gender=male&results=10&noinfo")
    suspend fun loadPlayers(): PlayerDTO
}
