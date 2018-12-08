package com.square.repos.data

import com.square.repos.data.local.LocalDatabase
import com.square.repos.data.remote.NetworkApi
import com.square.repos.model.Favorite
import com.square.repos.model.Repo
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.rxkotlin.Flowables
import io.reactivex.schedulers.Schedulers.io


class DataRepository(private val networkApi: NetworkApi, private val localDatabase: LocalDatabase) {

    fun fetchRepoDetails(repo: Repo): Flowable<Repo> =
            networkApi.fetchStargazers(repo.name!!).subscribeOn(io())
                    .observeOn(io())
                    .map { users ->
                        users.map { it.repoId = repo.id }
                        localDatabase.userDao().insertList(users)
                        System.out.println("DataRepository fetchRepoDetails 1 " + users)
                        repo
                    }
                    .mergeWith(Flowables.combineLatest(
                            localDatabase.repoDao().select(repo.id),
                            localDatabase.userDao().selectUsersRepo(repo.id))

                    { repo, users ->

                        repo.isSaved = localDatabase.favoriteDao().getFavorite(repoId = repo.id) != null
                        System.out.println("DataRepository fetchRepoDetails 2 $repo : $users")
                        repo.users = users
                        repo
                    })


    fun fetchRepos(): Flowable<List<Repo>> =
            networkApi.fetchList().subscribeOn(io())
                    .observeOn(io())
                    .doOnNext { repos ->
                        System.out.println("DataRepository fetchRepos 1 " + repos)
                        repos?.let {
                            localDatabase.repoDao().insertList(repos)
                        }
                    }
                    .mergeWith(localDatabase.repoDao().loadAll())
                    .doOnNext { repos ->
                        System.out.println("DataRepository fetchRepos 2 " + repos)
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