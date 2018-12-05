package com.square.repos.data.local

import android.arch.persistence.room.*
import com.square.repos.model.Repo
import com.square.repos.model.User
import io.reactivex.Flowable


@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertList(users: List<User>)

    @Delete
    fun delete(user: User)
}