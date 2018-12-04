package com.square.repos.app

import android.arch.persistence.room.Room
import com.square.repos.data.DataRepository
import com.square.repos.data.local.LocalDatabase
import com.square.repos.data.remote.NetworkApi
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val application: ApplicationController) {

    @Provides
    @Singleton
    fun getNetworkApi(): NetworkApi = NetworkApi()

    @Provides
    @Singleton
    fun getLocalDatabase(): LocalDatabase =
            Room.databaseBuilder(application, LocalDatabase::class.java, "square_db").build()

    @Provides
    @Singleton
    fun getDataRepository(networkApi: NetworkApi, localDatabase: LocalDatabase): DataRepository =
            DataRepository(networkApi, localDatabase)
}