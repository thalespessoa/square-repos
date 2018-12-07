package com.square.repos.viewmodel

import android.view.View
import com.square.repos.model.Repo
import com.square.repos.model.User

data class DetailRepoState(val loading: Boolean = false,
                           val repo: Repo? = null,
                           val users: List<User> = emptyList(),
                           val error: Throwable? = null) {
    val loadingVisibility = if (loading) View.VISIBLE else View.INVISIBLE
    val listRepoVisibility = if (!loading && error == null) View.VISIBLE else View.INVISIBLE
    val listErrorVisibility = if (!loading && error != null) View.VISIBLE else View.INVISIBLE
}