package com.square.repos.data.local

import android.arch.persistence.room.*
import com.square.repos.model.Repo
import com.square.repos.model.User
import io.reactivex.Flowable


@Dao
interface RepoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(repo: Repo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(repos: List<Repo>)

    @Delete
    fun delete(repo: Repo)

    @Query("SELECT * FROM repo")
    fun loadAll(): Flowable<List<Repo>>

    @Query("SELECT * FROM repo WHERE id > :id LIMIT 1")
    fun select(id:String): Flowable<Repo>
}