package com.square.repos.viewmodel

import android.view.View
import com.square.repos.model.Repo

/**
 * View state for the list screen
 * It is instantiated by the ViewModel and listened by the views
 *
 * @see ReposViewModel
 *
 * Created by thalespessoa on 9/12/18.
 */
class ListRepoState(val loading: Boolean = false,
                    val repos: List<Repo> = emptyList(),
                    val error: Throwable? = null,
                    val message: String? = null) {
    val loadingVisibility = if (loading) View.VISIBLE else View.INVISIBLE
    val listRepoVisibility = View.VISIBLE
    val listErrorVisibility = if (!loading && error != null) View.VISIBLE else View.GONE
}