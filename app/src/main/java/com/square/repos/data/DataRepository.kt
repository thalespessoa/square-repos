package com.square.repos.data

import com.square.repos.data.local.LocalDatabase
import com.square.repos.data.remote.NetworkApi
import com.square.repos.model.Favorite
import com.square.repos.model.Repo
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.rxkotlin.Flowables
import io.reactivex.schedulers.Schedulers.io

/**
 * Class responsible for manage(save, load and map) all data provided to the application.
 * This class should be inject by Dagger in any place of the
 * application that needs data access (remotely or locally)
 * The rest of application should never know if the data came from local db or from server.
 *
 * Created by thalespessoa on 9/12/18.
 */
class DataRepository(private val networkApi: NetworkApi, private val localDatabase: LocalDatabase) {

    /**
     * Fetch the list of “stargazers” of one repository
     * @return Flowable with repository users filled
     */
    fun fetchRepoDetails(repo: Repo): Flowable<Repo> =
            networkApi.fetchStargazers(repo.name!!).subscribeOn(io()) // Fetch stargazers from server
                    .observeOn(io())
                    .map { users -> // Update stargazers in local database
                        users.map { it.repoId = repo.id }
                        localDatabase.userDao().insertList(users)
                        repo
                    }
                    .mergeWith(Flowables.combineLatest(
                            localDatabase.repoDao().getRepoById(repo.id),
                            localDatabase.userDao().fetchUsersRepo(repo.id))

                    { localRepo, users -> // Check is repo is favorite
                        localRepo.isSaved = localDatabase.favoriteDao().getFavorite(repoId = localRepo.id) != null
                        localRepo.users = users // Add stargazers in repo
                        localRepo
                    })

    /**
     * Fetch the list of repositories
     * @return Flowable of repository list
     */
    fun fetchRepos(): Flowable<List<Repo>> =
            networkApi.fetchList().subscribeOn(io()) // Fetch repos from server
                    .observeOn(io())
                    .doOnNext { repos ->
                        repos?.let {
                            localDatabase.repoDao().insertList(repos) // Update repos local database
                        }
                    }
                    .mergeWith(localDatabase.repoDao().loadAll()) // Fetch repos from local database
                    .doOnNext { repos ->
                        localDatabase.favoriteDao().getFavorites().map { repoId ->
                            repos.find { it.id == repoId }?.isSaved = true
                        }
                    }

    /**
     * Save repository as favorite
     * @param Repo
     */
    fun saveRepo(repo: Repo): Completable = Completable.fromAction {
        localDatabase.runInTransaction {
            localDatabase.favoriteDao().insertFavorite(Favorite(repo.id))
            localDatabase.repoDao().insert(repo)
            repo.isSaved = true
        }
    }.subscribeOn(io())

    /**
     * Remove repository from favorites
     * @param Repo
     */
    fun removeRepo(repo: Repo): Completable = Completable.fromAction {
        localDatabase.runInTransaction {
            localDatabase.favoriteDao().deleteFavorite(Favorite(repo.id))
            localDatabase.repoDao().insert(repo)
            repo.isSaved = false
        }
    }.subscribeOn(io())
}