package com.square.repos.data.local

import android.arch.persistence.room.*
import com.square.repos.model.Repo
import com.square.repos.model.RepoUserJoin
import com.square.repos.model.User
import io.reactivex.Flowable


@Dao
interface RepoUserJoinDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(repoUserJoin: List<RepoUserJoin>)

    @Delete
    fun delete(repo: Repo)

    @Query("SELECT * FROM user INNER JOIN repo_user_join ON user.id=repo_user_join.userId WHERE repo_user_join.repoId=:repoId")
    fun selectUsersRepo(repoId:Int):Flowable<List<User>>
}