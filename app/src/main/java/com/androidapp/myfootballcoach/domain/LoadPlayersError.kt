package com.androidapp.myfootballcoach.domain

sealed class LoadPlayersError {
    object NoPlayersFound: LoadPlayersError()
    object NoInternet: LoadPlayersError()
    object SlowInternet: LoadPlayersError()
    object ServerError: LoadPlayersError()
}
