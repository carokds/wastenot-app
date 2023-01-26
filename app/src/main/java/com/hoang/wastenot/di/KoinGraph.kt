package com.hoang.wastenot.di

import com.hoang.wastenot.api.BarcodeServiceAPI
import com.hoang.wastenot.repositories.BarcodeRepository
import com.hoang.wastenot.repositories.UserRepository
import org.koin.dsl.module

object KoinGraph {
    val mainModules = module {
        single { UserRepository() }
        single { BarcodeServiceAPI() }
        single { BarcodeRepository() }
    }
}