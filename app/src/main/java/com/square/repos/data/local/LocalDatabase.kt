package com.square.repos.data.local

import android.app.Application
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import com.square.repos.model.Favorite
import com.square.repos.model.Repo
import com.square.repos.model.User

/**
 * Class responsible for the local database
 * All local data access comes from here.
 *
 * Created by thalespessoa on 9/12/18.
 */


class LocalDatabase(application: Application) {

    private val database = Room.databaseBuilder(application, SquareRoomDatabase::class.java, "square_db").build()

    // Users
    fun insertUserList(users: List<User>) = database.userDao().insertList(users)

    fun fetchUsersRepo(repoId: Int) = database.userDao().fetchUsersRepo(repoId)
    fun deleteRepo(user: User) = database.userDao().delete(user)

    // Repos
    fun insertRepoList(repos: List<Repo>) = database.repoDao().insertList(repos)
    fun loadRepoList() = database.repoDao().loadAll()
    fun getRepoById(id: Int) = database.repoDao().getRepoById(id)

    // Favorites
    fun getFavorites(): List<Int> = database.favoriteDao().getFavorites()

    fun getFavorite(repoId: Int) = database.favoriteDao().getFavorite(repoId)
    fun insertFavorite(repo: Repo) = database.runInTransaction {
        database.favoriteDao().insertFavorite(Favorite(repo.id))
        database.repoDao().insert(repo)
        repo.isSaved = true
    }

    fun deleteFavorite(repo: Repo) = database.runInTransaction {
        database.favoriteDao().deleteFavorite(Favorite(repo.id))
        database.repoDao().insert(repo)
        repo.isSaved = false
    }
}


@Database(entities = [Repo::class, User::class, Favorite::class],
        version = 1,
        exportSchema = false)
private abstract class SquareRoomDatabase : RoomDatabase() {
    abstract fun repoDao(): RepoDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun userDao(): UserDao
}
