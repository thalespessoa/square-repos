package com.square.repos.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "user_entity")
data class User(
        @PrimaryKey
        val id: Int,
        var repoId: Int? = null,
        val login: String? = null,
        val avatar_url: String? = null)