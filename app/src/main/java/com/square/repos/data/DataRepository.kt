package com.square.repos.data

import com.square.repos.data.local.LocalDatabase
import com.square.repos.data.remote.NetworkApi
import com.square.repos.model.Repo
import io.reactivex.Flowable
import java.util.concurrent.Executors


class DataRepository(
        private val networkApi: NetworkApi,
        private val localDatabase: LocalDatabase) {

    private val diskIO = Executors.newSingleThreadExecutor()

    fun fetchAllRepos(): Flowable<List<Repo>> = networkApi.fetchList()

    fun saveRepo(repo: Repo) {

    }

    fun fetchSavedRepos(): Flowable<List<Repo>> {
        return Flowable.just(listOf(Repo(1, "Repo 1"),
                Repo(2, "Repo 2"),
                Repo(3, "Repo 3")))
    }
}