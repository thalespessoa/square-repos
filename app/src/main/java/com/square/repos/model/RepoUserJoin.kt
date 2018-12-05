package com.square.repos.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "repo_user_join",
        primaryKeys = ["userId", "repoId"],
        foreignKeys = [
            ForeignKey(entity = Repo::class, parentColumns = arrayOf("id"), childColumns = arrayOf("repoId")),
            ForeignKey(entity = User::class, parentColumns = arrayOf("id"), childColumns = arrayOf("userId"))
        ])
data class RepoUserJoin(val userId: Int, val repoId: Int)