package com.square.repos.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.square.repos.app.ApplicationComponent
import com.square.repos.data.DataRepository
import com.square.repos.model.Repo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.reactivestreams.Subscriber
import javax.inject.Inject

class ReposViewModel : ViewModel(), ApplicationComponent.Injectable {

    @Inject
    lateinit var dataRepository: DataRepository

    val listState = MutableLiveData<ListRepoState>().apply { value = ListRepoState(false) }
    val detailState = MutableLiveData<DetailRepoState>().apply { value = DetailRepoState(false) }

    private var selectedRepoSubscription:Disposable? = null
    private var listRepoSubscription:Disposable? = null

    @Override
    override fun inject(applicationComponent: ApplicationComponent) {
        applicationComponent.inject(this)
        listRepoSubscription = dataRepository.fetchRepos()
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map {
                    ListRepoState(false, it)
                }
                .onErrorReturn { error ->
                    listState.value?.let {
                        ListRepoState(false, it.repos, error)
                    } ?: ListRepoState(false, error = error)

                }
                .subscribe {
                    listState.value = it
                }
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
                            println("Error Save repo: ${it}")
                            // error
                        }
            }
        }
    }

    fun selectRepo(repo: Repo) {
        detailState.value = DetailRepoState(false, repo)
        selectedRepoSubscription?.dispose()
        selectedRepoSubscription = dataRepository.fetchRepoDetails(repo)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map { repo ->
                    DetailRepoState(false, repo)
                }
                .onErrorReturn {error ->
                    detailState.value?.let {
                        DetailRepoState(false, it.repo, error)
                    } ?: DetailRepoState(false, error = error)
                }
                .subscribe {
                    detailState.value = it
                }
    }

    override fun onCleared() {
        super.onCleared()
        listRepoSubscription?.dispose()
        selectedRepoSubscription?.dispose()
    }
}