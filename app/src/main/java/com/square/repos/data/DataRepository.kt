package com.square.repos.data

import com.square.repos.data.local.LocalDatabase
import com.square.repos.data.remote.NetworkApi
import com.square.repos.model.Favorite
import com.square.repos.model.Repo
import com.square.repos.model.User
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.rxkotlin.Flowables
import io.reactivex.schedulers.Schedulers.io


class DataRepository(private val networkApi: NetworkApi, private val localDatabase: LocalDatabase) {

//    fun fetchRepoDetails(repo: Repo): Flowable<Repo> = Flowable.just(Repo(2, "Repo 2").apply {
//        listOf(User(1, 1, "User 1"))
//    })
    fun fetchRepoDetails(repo: Repo): Flowable<Repo> =
            networkApi.fetchStargazers(repo.name!!).subscribeOn(io())
                    .observeOn(io())
                    .map { users ->
                        users.map { it.repoId = repo.id }
                        localDatabase.userDao().insertList(users)
                        repo
                    }
                    .mergeWith(Flowables.combineLatest(
                            localDatabase.repoDao().select(repo.id),
                            localDatabase.userDao().selectUsersRepo(repo.id))

                    { localRepo, users ->
                        localRepo.isSaved = localDatabase.favoriteDao().getFavorite(repoId = localRepo.id) != null
                        localRepo.users = users
                        localRepo
                    })

//    fun fetchRepos(): Flowable<List<Repo>> = Flowable.just(listOf(Repo(1, "Repo1"), Repo(2, "Repo2")))
    fun fetchRepos(): Flowable<List<Repo>> =
            networkApi.fetchList().subscribeOn(io())
                    .observeOn(io())
                    .doOnNext { repos ->
                        repos?.let {
                            localDatabase.repoDao().insertList(repos)
                        }
                    }
                    .mergeWith(localDatabase.repoDao().loadAll())
                    .doOnNext { repos ->
                        localDatabase.favoriteDao().getFavorites().map { repoId ->
                            repos.find { it.id == repoId }?.isSaved = true
                        }
                    }

    fun saveRepo(repo: Repo): Completable = Completable.fromAction {
        localDatabase.runInTransaction {
            localDatabase.favoriteDao().insertFavorite(Favorite(repo.id))
            localDatabase.repoDao().insert(repo)
        }
    }.subscribeOn(io())

    fun removeRepo(repo: Repo): Completable = Completable.fromAction {
        localDatabase.runInTransaction {
            localDatabase.favoriteDao().deleteFavorite(Favorite(repo.id))
            localDatabase.repoDao().insert(repo)
        }
    }.subscribeOn(io())
}