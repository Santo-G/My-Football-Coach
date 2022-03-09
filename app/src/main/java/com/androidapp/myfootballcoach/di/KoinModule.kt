package com.androidapp.myfootballcoach.di

import com.androidapp.myfootballcoach.domain.PlayerRepository
import com.androidapp.myfootballcoach.domain.PlayerRepositoryImpl
import com.androidapp.myfootballcoach.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

// declaring all the dependencies that will be injected in other components

val appModule = module {
    /* creating a Singleton Pattern repository */
    single<PlayerRepository> {
        PlayerRepositoryImpl(api = get())
    }

    // creating ViewModel objects
    viewModel { HomeViewModel(repository = get()) }

}
