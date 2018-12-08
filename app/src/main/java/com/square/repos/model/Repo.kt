package com.square.repos.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "repo_entity")
data class Repo(
        @PrimaryKey
        val id: Int,
        val name: String? = null,
        val stargazers_count: String? = null) {

        @Ignore var isSaved:Boolean = false
        @Ignore var users:List<User> = arrayListOf()

        override fun equals(other: Any?): Boolean = if (other is Repo) other.id == id
        else super.equals(other)
}