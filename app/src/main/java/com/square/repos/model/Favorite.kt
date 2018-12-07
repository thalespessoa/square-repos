package com.square.repos.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey


@Entity(tableName = "favorite",
        primaryKeys = ["repoId"],
        foreignKeys = [
            ForeignKey(entity = Repo::class,
                    parentColumns = arrayOf("id"),
                    childColumns = arrayOf("repoId"))
        ])
data class Favorite(val repoId: Int)