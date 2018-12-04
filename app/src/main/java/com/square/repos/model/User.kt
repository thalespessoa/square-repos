package com.square.repos.model

import android.arch.persistence.room.PrimaryKey

data class User(
        @PrimaryKey
        val id:Int,
        val login: String? = null,
        val avatar_url: String? = null)