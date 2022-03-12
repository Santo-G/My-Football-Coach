package com.androidapp.myfootballcoach.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androidapp.myfootballcoach.domain.LoadPlayersError
import com.androidapp.myfootballcoach.domain.LoadPlayersResult
import com.androidapp.myfootballcoach.domain.Player
import com.androidapp.myfootballcoach.domain.PlayerRepository
import com.androidapp.myfootballcoach.utils.SingleLiveEvent
import kotlinx.coroutines.launch
import timber.log.Timber

class HomeViewModel(
    private val repository: PlayerRepository
) : ViewModel() {

    // MutableLiveData: container of T type, T represents a state
    // states is a variable that our activity can observe
    // when state changes this variable is set accordingly
    val states = MutableLiveData<HomeScreenStates>()
    val actions = SingleLiveEvent<HomeScreenActions>()

    // Check the type of event and acts accordingly
    fun send(event: HomeScreenEvents) {
        Timber.d(event.toString())

        when (event) {
            is HomeScreenEvents.OnReady -> {
                loadContent()
            }
            is HomeScreenEvents.OnPlayerClick -> {
                actions.postValue(HomeScreenActions.NavigateToDetail(event.player))
            }
            is HomeScreenEvents.OnRefreshClicked -> {
                loadContent()
            }
        }


    }

    private fun loadContent() {
        states.postValue(HomeScreenStates.Loading)
        viewModelScope.launch {
            val playerResult = repository.loadPlayers()
            when (playerResult) {
                is LoadPlayersResult.Failure -> onPlayersFailure(playerResult)
                is LoadPlayersResult.Success -> {
                    val players = playerResult.players.map {
                        Player(
                            name = it.name,
                            picture = it.picture
                        )
                    }
                    states.postValue(HomeScreenStates.Content(players))
                }
            }
        }
    }

    private fun onPlayersFailure(playerResult: LoadPlayersResult.Failure) {
        when (playerResult.error) {
            LoadPlayersError.NoPlayersFound -> states.postValue(HomeScreenStates.Error(ErrorStates.ShowNoPlayerFound))
            LoadPlayersError.ServerError -> states.postValue(HomeScreenStates.Error(ErrorStates.ShowServerError))

        }

    }
}


/**
 * Contains all objects that represents the state in which the actual screen can be found
 * Loading, Error, Content
 */
sealed class HomeScreenStates {
    object Loading : HomeScreenStates()
    data class Error(val error: ErrorStates) : HomeScreenStates()
    data class Content(val playersList: List<Player>) : HomeScreenStates()
}

sealed class ErrorStates {
    object ShowNoPlayerFound : ErrorStates()
    object ShowServerError : ErrorStates()
}

sealed class HomeScreenActions {
    data class NavigateToDetail(val player: Player) : HomeScreenActions()
    /*
    class NavigateToSearch(val from: String?, val to: String?) : HomeScreenActions()
    */
}

// Contains all events that can be sent to ViewModel
sealed class HomeScreenEvents {
    data class OnPlayerClick(val player: Player) : HomeScreenEvents()
    object OnReady : HomeScreenEvents()
    object OnRefreshClicked : HomeScreenEvents()
}
