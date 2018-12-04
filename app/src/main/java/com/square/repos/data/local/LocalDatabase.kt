package com.square.repos.data.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.square.repos.model.Repo

/**
 * Created by thalespessoa on 3/29/18.
 */


@Database(entities = arrayOf(Repo::class), version = 1, exportSchema = false)
abstract class LocalDatabase : RoomDatabase()
