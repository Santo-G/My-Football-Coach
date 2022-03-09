package com.androidapp.myfootballcoach.domain

sealed class LoadPlayersResult {
    data class Success(val players: List<Player>): LoadPlayersResult()
    data class Failure(val error: LoadPlayersError): LoadPlayersResult()
}
