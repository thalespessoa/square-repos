package com.square.repos.data.local

import android.arch.persistence.room.*
import com.square.repos.model.Favorite


@Dao
interface FavoriteDao {

    @Query("SELECT * FROM favorite_entity")
    fun getFavorites(): List<Int>

    @Query("SELECT * FROM favorite_entity WHERE repoId = :repoId LIMIT 1")
    fun getFavorite(repoId:Int): Favorite?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavorite(favorite: Favorite)

    @Delete()
    fun deleteFavorite(favorite: Favorite)
}