package com.square.repos.data.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.square.repos.model.Repo
import com.square.repos.model.RepoUserJoin
import com.square.repos.model.User

/**
 * Created by thalespessoa on 3/29/18.
 */


@Database(entities = [Repo::class, User::class, RepoUserJoin::class],
        version = 1,
        exportSchema = false)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun repoDao(): RepoDao
    abstract fun userDao(): UserDao
    abstract fun repoUserJoinDao(): RepoUserJoinDao
}
