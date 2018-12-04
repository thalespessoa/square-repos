package com.square.repos.viewmodels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.view.View
import com.square.repos.app.ApplicationComponent
import com.square.repos.data.DataRepository
import com.square.repos.model.Repo
import com.square.repos.model.User
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ReposViewModel : ViewModel(), ApplicationComponent.Injectable {

    @Inject
    lateinit var dataRepository: DataRepository

    data class ListViewState(val loading: Boolean = false, val listRepo: List<Repo> = emptyList(), val error: Throwable? = null) {
        val loadingVisibility = if (loading) View.VISIBLE else View.INVISIBLE
        val listRepoVisibility = if (!loading && error == null) View.VISIBLE else View.INVISIBLE
        val listErrorVisibility = if (!loading && error != null) View.VISIBLE else View.INVISIBLE
    }

    val listState = MutableLiveData<ListViewState>()

    private val disposables: CompositeDisposable = CompositeDisposable()

    @Override
    override fun inject(applicationComponent: ApplicationComponent) {
        applicationComponent.inject(this)
        if(listState.value == null) {
            fetchListRepos()
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

    private fun fetchListRepos() {
        listState.value = ListViewState(true)
        val repos = arrayListOf<Repo>()

        dataRepository.fetchSavedRepos()
                .mergeWith(dataRepository.fetchAllRepos())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map {
                    repos.addAll(it)
                    ListViewState(false, repos)
                }
                .onErrorReturn {
                    ListViewState(false, error = it)
                }
                .subscribe {
                    listState.value = it
                }
    }

    fun selectRepo(repo: Repo) {
        print("Select repos: ${repo}")
    }
}