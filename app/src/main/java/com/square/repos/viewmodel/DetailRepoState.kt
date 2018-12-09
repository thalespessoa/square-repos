package com.square.repos.viewmodel

import android.view.View
import com.square.repos.R
import com.square.repos.model.Repo
import retrofit2.HttpException
import org.json.JSONObject


/**
 * View state for the detail screen
 * It is instantiated by the ViewModel and listened by the views
 *
 * @see ReposViewModel
 *
 * Created by thalespessoa on 9/12/18.
 */
data class DetailRepoState(val loading: Boolean = false,
                           val repo: Repo? = null,
                           val error: Throwable? = null) {
    val loadingVisibility = if (loading && repo?.users?.isEmpty() != false && error == null)
        View.VISIBLE
    else
        View.INVISIBLE
    val errorVisibility = if (!loading && error != null) View.VISIBLE else View.GONE
    val subtitleVisibility = if (!loading && error != null) View.INVISIBLE else View.VISIBLE

    val users = repo?.users ?: listOf()

    val favImage = when {
        repo?.isSaved == true -> R.drawable.baseline_favorite_24px
        repo?.isSaved == false -> R.drawable.baseline_favorite_border_24px
        else -> 0
    }

    val errorMessage = if (error is HttpException) {
        try {
            val jsonObject = JSONObject(error.response().errorBody()?.string())
            jsonObject.getString("message")
        } catch (e: Exception) {
            error.message
        }
    } else {
        error?.message
    }
}