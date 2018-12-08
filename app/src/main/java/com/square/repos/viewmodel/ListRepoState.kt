package com.square.repos.viewmodel

import android.view.View
import com.square.repos.model.Repo

class ListRepoState(val loading: Boolean = false,
                    val repos: List<Repo> = emptyList(),
                    val error: Throwable? = null,
                    val message: String? = null) {
    val loadingVisibility = if (loading) View.VISIBLE else View.INVISIBLE
    val listRepoVisibility = View.VISIBLE
    val listErrorVisibility = if (!loading && error != null) View.VISIBLE else View.GONE
}