package com.square.repos.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.square.repos.app.ApplicationComponent
import com.square.repos.data.DataRepository
import com.square.repos.model.Repo
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.Flowables
import io.reactivex.rxkotlin.Observables
import io.reactivex.schedulers.Schedulers
import java.util.function.BiFunction
import javax.inject.Inject

class ReposViewModel : ViewModel(), ApplicationComponent.Injectable {

    @Inject
    lateinit var dataRepository: DataRepository

    val listState = MutableLiveData<ListRepoState>().apply { value = ListRepoState(false) }
    val detailState = MutableLiveData<DetailRepoState>()

    private val disposables: CompositeDisposable = CompositeDisposable()

    @Override
    override fun inject(applicationComponent: ApplicationComponent) {
        applicationComponent.inject(this)
        fetchListRepos()
        if (detailState.value == null) {
            detailState.value = DetailRepoState(false)
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

    fun saveRepo() {
        detailState.value?.let { detailState ->
            detailState.repo?.let { repo ->
                dataRepository.saveRepo(repo, detailState.users)
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

    private fun fetchListRepos() {
        Flowables.combineLatest(
                dataRepository.fetchSavedRepos().startWith(listOf<Repo>()),
                dataRepository.fetchAllRepos().startWith(listOf<Repo>()),
                { localRepos, remoteRepos ->
                    val combined = localRepos.plus(remoteRepos.minus(localRepos))
                    ListRepoState(false, combined)
                })
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn { error ->
                    listState.value?.let {
                        ListRepoState(false, it.repos, error)
                    } ?: ListRepoState(false, error = error)

                }
                .mergeWith {

                }
                .subscribe {
                    listState.value = it
                }
    }

    fun selectRepo(repo: Repo) {
        repo.name?.let { repoName ->
            detailState.value = DetailRepoState(false, repo)
            dataRepository.fetchSavedUsers(repo.id)
                    .mergeWith(dataRepository.fetchUsers(repoName))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map {
                        DetailRepoState(false, repo, it, null)
                    }
                    .onErrorReturn {
                        DetailRepoState(false, error = it)
                    }
                    .subscribe {
                        detailState.value = it
                    }
        }
    }
}