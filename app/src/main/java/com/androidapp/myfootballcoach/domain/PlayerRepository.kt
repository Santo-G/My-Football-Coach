package com.androidapp.myfootballcoach.domain


interface PlayerRepository {
    suspend fun loadPlayers(): LoadPlayersResult
}


class PlayerRepositoryImpl(
    private val api: PlayerAPI,
): PlayerRepository {

    override suspend fun loadPlayers(): LoadPlayersResult {
        return api.loadPlayers()
    }
}
