package com.square.repos.data

import android.annotation.SuppressLint
import com.square.repos.data.local.LocalDatabase
import com.square.repos.data.remote.NetworkApi
import com.square.repos.model.Repo
import com.square.repos.model.RepoUserJoin
import com.square.repos.model.User
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.schedulers.Schedulers.io


class DataRepository(private val networkApi: NetworkApi, private val localDatabase: LocalDatabase) {

    fun fetchUsers(repoName: String): Flowable<List<User>> = networkApi.fetchStargazers(repoName)
    fun fetchAllRepos(): Flowable<List<Repo>> = networkApi.fetchList()
            .subscribeOn(io())
            .observeOn(mainThread())

    fun fetchSavedRepos(): Flowable<List<Repo>> =
            localDatabase.repoDao().loadAll()
                    .subscribeOn(io())
                    .observeOn(mainThread())


    fun fetchRepos(): Flowable<List<Repo>> =
            localDatabase.repoDao().loadAll()
                    .subscribeOn(io())
                    .observeOn(mainThread())

    fun fetchSavedUsers(repoId: Int): Flowable<List<User>> =
            localDatabase.repoUserJoinDao().selectUsersRepo(repoId)
                    .subscribeOn(io())
                    .observeOn(mainThread())


    fun saveRepo(repo: Repo, users: List<User>): Completable = Completable.fromAction {
        localDatabase.runInTransaction {
            localDatabase.repoDao().insert(repo.apply { isSaved = true })
            localDatabase.userDao().insertList(users)
            localDatabase.repoUserJoinDao().insert(users.map {
                RepoUserJoin(it.id, repo.id)
            })
        }
    }
            .observeOn(mainThread())
            .subscribeOn(io())

    @SuppressLint("CheckResult")
    fun fetchRepos(): Flowable<List<Repo>> {
        networkApi.fetchList()
                .subscribeOn(Schedulers.io())
                .subscribe {
                    localDatabase.repoDao().insertList(it)
                }
        return localDatabase.repoDao().loadAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    // Test

//    fun fetchAllRepos(): Flowable<List<Repo>> = Flowable.just(listOf(Repo(1, "Repo 4"),
//            Repo(2, "Repo 5"),
//            Repo(3, "Repo 6")))

//    fun fetchSavedRepos(): Flowable<List<Repo>> = Flowable.just(listOf(Repo(1, "Repo 1"),
//            Repo(2, "Repo 2"),
//            Repo(3, "Repo 3")))

    fun fetchUsers(repoId: Int): Flowable<List<User>> = Flowable.just(listOf(User(1, "User 1"),
            User(2, "User 2"),
            User(3, "User 3")))

}