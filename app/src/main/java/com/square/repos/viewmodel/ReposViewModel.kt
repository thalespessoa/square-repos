package com.square.repos.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.square.repos.app.ApplicationComponent
import com.square.repos.data.DataRepository
import com.square.repos.model.Repo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class ReposViewModel : ViewModel(), ApplicationComponent.Injectable {

    @Inject
    lateinit var dataRepository: DataRepository

    val listState by lazy { MutableLiveData<ListRepoState>().apply { value = ListRepoState(false) } }
    val detailState by lazy { MutableLiveData<DetailRepoState>().apply { value = DetailRepoState(false) } }

    private var selectedRepoSubscription: Disposable? = null
    private var listRepoSubscription: Disposable? = null

    @Override
    override fun inject(applicationComponent: ApplicationComponent) {
        applicationComponent.inject(this)
        listRepos()
    }

    fun listRepos() {
        listState.value = ListRepoState(true)
        listRepoSubscription = dataRepository.fetchRepos()
                .observeOn(AndroidSchedulers.mainThread())
                .map {
                    ListRepoState(false, it)
                }
                .onErrorReturn { error ->
                    ListRepoState(false, listState.value?.repos ?: listOf(), error)
                }
                .subscribe({
                    listState.value = it
                }, { error ->
                    print("subscribe error: ${error}")
                    ListRepoState(false, listState.value?.repos ?: listOf(), error)
                })
    }

    fun selectRepo(repoSelected: Repo) {
        detailState.value = DetailRepoState(true, repoSelected)
        selectedRepoSubscription?.dispose()
        selectedRepoSubscription = dataRepository.fetchRepoDetails(repoSelected)
                .observeOn(AndroidSchedulers.mainThread())
                .map { repo ->
                    DetailRepoState(false, repo)
                }
                .onErrorReturn { error ->
                    DetailRepoState(false, detailState.value?.repo, error)
                }
                .subscribe({
                    detailState.value = it
                }, { error ->
                    DetailRepoState(false, detailState.value?.repo, error)
                })
    }

    fun saveRepo() {
        detailState.value?.let { detailState ->
            detailState.repo?.let { repo ->
                if (repo.isSaved) {
                    dataRepository.removeRepo(repo)
                } else {
                    dataRepository.saveRepo(repo)
                }
                        .subscribe({
                            // success
                            println("Save repo")
                        }) {
                            it.printStackTrace()
                            println("Error Save repo: $it")
                            // error
                        }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        listRepoSubscription?.dispose()
        selectedRepoSubscription?.dispose()
    }
}