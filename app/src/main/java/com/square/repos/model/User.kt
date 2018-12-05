package com.square.repos.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class User(
        @PrimaryKey
        val id:Int,
        val login: String? = null,
        val avatar_url: String? = null)