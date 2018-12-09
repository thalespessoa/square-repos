package com.square.repos.data.local

import android.arch.persistence.room.*
import com.square.repos.model.Repo
import io.reactivex.Flowable


@Dao
interface RepoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(repo: Repo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(repos: List<Repo>)

    @Delete
    fun delete(repo: Repo)

    @Query("SELECT * FROM repo_entity")
    fun loadAll(): Flowable<List<Repo>>

    @Query("SELECT * FROM repo_entity WHERE id = :id LIMIT 1")
    fun getRepoById(id:Int): Flowable<Repo>

    @Update()
    fun update(repos: List<Repo>)
}