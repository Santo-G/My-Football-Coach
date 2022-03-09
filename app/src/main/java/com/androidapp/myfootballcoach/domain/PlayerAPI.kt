package com.androidapp.myfootballcoach.domain

interface PlayerAPI {
    suspend fun loadPlayers(): LoadPlayersResult
}
