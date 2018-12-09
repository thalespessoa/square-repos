package com.square.repos

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.square.repos.data.DataRepository
import com.square.repos.data.local.LocalDatabase
import com.square.repos.data.remote.NetworkApi
import com.square.repos.model.Favorite
import com.square.repos.model.Repo
import com.square.repos.model.User
import io.reactivex.Flowable
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.TestSubscriber
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule


class DataRepositoryTests {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private val fakeFavorite = Favorite(1)
    private val fakeRepo = Repo(1, "Repo 1")
    private val fakeList = listOf(Repo(1, "Repo 1"))
    private val fakeStargazers = listOf(User(1, 1, "User 1"))
    private val fakeRepoUser = Repo(1, "Repo 1").apply { users = listOf(User(1, 1, "User 1")) }

    private val networkApi = mock<NetworkApi> {
        on { fetchList() }.thenReturn(Flowable.just(fakeList))
        on { fetchStargazers(any()) }.thenReturn(Flowable.just(fakeStargazers))
    }
    private val database = mock<LocalDatabase> {
        on { loadRepoList() }.thenReturn(Flowable.just(fakeList))
        on { fetchUsersRepo(any()) }.thenReturn(Flowable.just(fakeStargazers))
        on { getRepoById(any()) }.thenReturn(Flowable.just(fakeRepo))
        on { getFavorite(any()) }.thenReturn(fakeFavorite)
    }

    private val dataRepository = DataRepository(networkApi, database)

    @Before
    fun setup() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { _ -> Schedulers.trampoline() }
    }

    @Test
    fun testRepoList() {
        val subscriber = TestSubscriber<List<Repo>>()
        dataRepository.fetchRepos().subscribe(subscriber)
        subscriber.assertNoErrors()
        subscriber.assertValueCount(1)
    }

    @Test
    fun testStargazerList() {
        val subscriber = TestSubscriber<Repo>()
        dataRepository.fetchRepoDetails(Repo(1, "Repo 1")).subscribe(subscriber)
        subscriber.assertNoErrors()
    }
}