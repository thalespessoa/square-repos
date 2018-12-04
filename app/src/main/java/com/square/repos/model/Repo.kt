package com.square.repos.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class Repo(
        @PrimaryKey
        val id:Int,
        val name: String? = null,
        val stargazers_count: String? = null)