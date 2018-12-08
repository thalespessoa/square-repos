package com.square.repos.data.local

import android.arch.persistence.room.*
import com.square.repos.model.User
import io.reactivex.Flowable


@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(users: List<User>)

    @Delete
    fun delete(user: User)

    @Query("SELECT * FROM user_entity WHERE repoId=:repoId")
    fun selectUsersRepo(repoId:Int):Flowable<List<User>>
}