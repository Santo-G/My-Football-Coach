package com.androidapp.myfootballcoach.networking

import com.androidapp.myfootballcoach.domain.LoadPlayersError
import com.androidapp.myfootballcoach.domain.LoadPlayersResult
import com.androidapp.myfootballcoach.domain.Player
import com.androidapp.myfootballcoach.domain.PlayerAPI
import com.androidapp.myfootballcoach.networking.api.Result
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.io.IOException
import java.net.SocketTimeoutException

class PlayerAPIImpl : PlayerAPI {
    private val service: PlayerService

    init {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://randomuser.me/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        service = retrofit.create(PlayerService::class.java)
    }

    override suspend fun loadPlayers(): LoadPlayersResult {
        // try-catch for networking error management
        try {
            val playersList = service.loadPlayers()
            val players = playersList.results.mapNotNull {
                it.toDomain()
            }
            return if (players.isEmpty()) {
                LoadPlayersResult.Failure(LoadPlayersError.NoPlayersFound)
            } else {
                LoadPlayersResult.Success(players)
            }
        } catch (e: IOException) {
            Timber.e("IOException", e.message.toString())
            return LoadPlayersResult.Failure(LoadPlayersError.ServerError)
        } catch (e: SocketTimeoutException) {
            Timber.e("SocketTimeoutException", e.message.toString())
            return LoadPlayersResult.Failure(LoadPlayersError.SlowInternet)
        } catch (e: Exception) {
            Timber.e(e, "Generic Exception on LoadPlayers", e.message.toString())
            return LoadPlayersResult.Failure(LoadPlayersError.ServerError)
        }
    }

    private fun Result.toDomain(): Player {
        return Player(
            name = name,
            picture = picture
        )
    }
}
