package com.square.repos.viewmodel

import android.view.View
import com.square.repos.R
import com.square.repos.model.Repo
import retrofit2.HttpException

data class DetailRepoState(val loading: Boolean = false,
                           val repo: Repo? = null,
                           val error: Throwable? = null) {
    val loadingVisibility = if (loading) View.VISIBLE else View.INVISIBLE
    val listRepoVisibility = if (!loading && error == null) View.VISIBLE else View.INVISIBLE
    val listErrorVisibility = if (!loading && error != null) View.VISIBLE else View.GONE

    val users = repo?.users ?: listOf()

    val favImage = if (repo?.isSaved == true)
        R.drawable.baseline_favorite_24px
    else
        R.drawable.baseline_favorite_border_24px
}