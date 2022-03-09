package com.androidapp.myfootballcoach.di

import com.androidapp.myfootballcoach.domain.PlayerAPI
import com.androidapp.myfootballcoach.networking.PlayerAPIImpl
import org.koin.dsl.module

val networkingKoinModule = module {
    single<PlayerAPI> {
        PlayerAPIImpl()
    }
}
