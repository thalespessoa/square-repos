package com.square.repos.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class Repo(
        @PrimaryKey
        val id: Int,
        val name: String? = null,
        val stargazers_count: String? = null,
        var isSaved: Boolean = false) {

    override fun equals(other: Any?): Boolean = if (other is Repo) other.id == id
    else super.equals(other)
}