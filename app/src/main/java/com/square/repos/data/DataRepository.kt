package com.square.repos.data

import com.square.repos.data.local.LocalDatabase
import com.square.repos.data.remote.NetworkApi
import com.square.repos.model.Repo
import com.square.repos.model.RepoUserJoin
import com.square.repos.model.User
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Executors


class DataRepository(private val networkApi: NetworkApi, private val localDatabase: LocalDatabase) {

    private val diskIO = Executors.newSingleThreadExecutor()


    fun fetchUsers(repoName: String): Flowable<List<User>> = networkApi.fetchStargazers(repoName)
    fun fetchAllRepos(): Flowable<List<Repo>> = networkApi.fetchList()

    fun fetchSavedRepos(): Flowable<List<Repo>> = localDatabase.repoDao().loadAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun fetchSavedUsers(repoId: Int): Flowable<List<User>> = localDatabase.repoUserJoinDao().selectUsersRepo(repoId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun saveRepo(repo: Repo, users: List<User>) {
        diskIO.execute {
            localDatabase.repoDao().insert(repo)
            localDatabase.userDao().insertList(users)
            val repoUserJoinList = arrayListOf<RepoUserJoin>()
            users.forEach {
                repoUserJoinList.add(RepoUserJoin(it.id, repo.id))
            }
            localDatabase.repoUserJoinDao().insert(repoUserJoinList)
        }
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