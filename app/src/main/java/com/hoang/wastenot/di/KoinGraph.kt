package com.hoang.wastenot.di

import com.hoang.wastenot.repositories.UserRepository
import org.koin.dsl.module

object KoinGraph {
    val mainModules = module {
        single { UserRepository() }
    }
}