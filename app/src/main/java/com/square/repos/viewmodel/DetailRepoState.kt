package com.square.repos.viewmodel

import android.view.View
import com.square.repos.R
import com.square.repos.model.Repo

data class DetailRepoState(val loading: Boolean = false,
                           val repo: Repo? = null,
                           val error: Throwable? = null) {
    val loadingVisibility = if (loading) View.VISIBLE else View.INVISIBLE
    val listRepoVisibility = if (!loading && error == null) View.VISIBLE else View.INVISIBLE
    val listErrorVisibility = if (!loading && error != null) View.VISIBLE else View.GONE

    val users = repo?.users ?: listOf()

    val favImage = when {
        repo?.isSaved == true -> R.drawable.baseline_favorite_24px
        repo?.isSaved == false -> R.drawable.baseline_favorite_border_24px
        else -> 0
    }
}