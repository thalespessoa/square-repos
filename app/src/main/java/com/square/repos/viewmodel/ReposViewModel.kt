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

/**
 * ViewModel responsible to the communication between data providers and views.
 * It communicate with 'DataRepository' class to read and write data
 * and provide view states objects(ListRepoState and DetailRepoState) for the views
 *
 * @see DataRepository
 * @see ListRepoState
 * @see DetailRepoState
 *
 * Created by thalespessoa on 9/12/18.
 */
class ReposViewModel : ViewModel(), ApplicationComponent.Injectable {

    @Inject
    lateinit var dataRepository: DataRepository

    /**
     * View state for repository list screen
     */
    val listState by lazy { MutableLiveData<ListRepoState>().apply { value = ListRepoState(false) } }

    /**
     * View state for repository detail screen
     */
    val detailState by lazy { MutableLiveData<DetailRepoState>().apply { value = DetailRepoState(false) } }

    private var selectedRepoSubscription: Disposable? = null
    private var listRepoSubscription: Disposable? = null

    @Override
    override fun inject(applicationComponent: ApplicationComponent) {
        applicationComponent.inject(this)
        listRepos()
    }

    /**
     * Load list of repositories
     */
    fun listRepos() {
        listRepoSubscription = dataRepository.fetchRepos()
                .observeOn(AndroidSchedulers.mainThread())
                .map {
                    ListRepoState(false, it)
                }
                .startWith(ListRepoState(true))
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

    /**
     * Select a repository and load it stargazers
     */
    fun selectRepo(repoSelected: Repo) {
        selectedRepoSubscription?.dispose()
        selectedRepoSubscription = dataRepository.fetchRepoDetails(repoSelected)
                .observeOn(AndroidSchedulers.mainThread())
                .map { repo ->
                    DetailRepoState(false, repo)
                }
                .startWith(DetailRepoState(true))
                .onErrorReturn { error ->
                    DetailRepoState(false, detailState.value?.repo, error)
                }
                .subscribe({
                    detailState.value = it
                }, { error ->
                    DetailRepoState(false, detailState.value?.repo, error)
                })
    }

    /**
     * Save selected repository as favorite
     */
    fun saveRepo() {
        detailState.value?.let { state ->
            state.repo?.let { repo ->
                if (repo.isSaved) {
                    dataRepository.removeRepo(repo)
                } else {
                    dataRepository.saveRepo(repo)
                }
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            detailState.value = state.copy().apply { repo.isSaved = true }
                        }) {
                            it.printStackTrace()
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