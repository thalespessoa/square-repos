package com.square.repos

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.square.repos.data.DataRepository
import com.square.repos.model.Repo
import com.square.repos.model.User
import com.square.repos.viewmodel.ReposViewModel
import io.reactivex.Flowable
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import java.util.concurrent.TimeUnit


class ReposViewModelTests {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private val fakeList = listOf(Repo(1, "Repo 1"))
    private val fakeRepo = Repo(2, "Repo 2").apply {
        listOf(User(1, 1, "User 1"))
    }

    private val dataRepositoryMock = mock<DataRepository>()
    private val viewModel = ReposViewModel()

    @Before
    fun setup() {
        viewModel.dataRepository = dataRepositoryMock
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { _ -> Schedulers.trampoline() }
    }

    @Test
    fun testListSuccess() {
        val state = viewModel.listState
        whenever(dataRepositoryMock.fetchRepos()).thenReturn(Flowable.just(fakeList))
        viewModel.listRepos()
        Assert.assertEquals(false, state.value?.loading)
        Assert.assertNull(state.value?.error)
        Assert.assertEquals(fakeList, state.value?.repos)
    }

    @Test
    fun testListError() {
        val state = viewModel.listState
        whenever(dataRepositoryMock.fetchRepos()).thenReturn(Flowable.error(Throwable("message")))
        viewModel.listRepos()
        Assert.assertEquals(false, state.value?.loading)
        Assert.assertEquals("message", state.value?.error?.message)
        Assert.assertEquals(true, state.value?.repos?.isEmpty())
    }

    @Test
    fun testListLoading() {
        val state = viewModel.listState
        whenever(dataRepositoryMock.fetchRepos()).thenReturn(Flowable.just(fakeList)
                .delay(100, TimeUnit.MILLISECONDS))
        viewModel.listRepos()
        Assert.assertEquals(true, state.value?.loading)
        Assert.assertNull(state.value?.error)
        Assert.assertEquals(true, state.value?.repos?.isEmpty())
        Thread.sleep(150)
        Assert.assertEquals(false, state.value?.loading)
        Assert.assertNull(state.value?.error)
        Assert.assertEquals(fakeList, state.value?.repos)
    }

    @Test
    fun testDetailSuccess() {
        val state = viewModel.detailState
        whenever(dataRepositoryMock.fetchRepoDetails(fakeRepo)).thenReturn(Flowable.just(fakeRepo))
        viewModel.selectRepo(fakeRepo)
        Assert.assertEquals(false, state.value?.loading)
        Assert.assertNull(state.value?.error)
        Assert.assertEquals(fakeRepo, state.value?.repo)
    }

    @Test
    fun testDetailError() {
        val state = viewModel.detailState
        whenever(dataRepositoryMock.fetchRepoDetails(fakeRepo)).thenReturn(Flowable.error(Throwable("message")))
        viewModel.selectRepo(fakeRepo)
        Assert.assertEquals(false, state.value?.loading)
        Assert.assertEquals("message", state.value?.error?.message)
        Assert.assertNotNull(state.value?.repo)
    }

    @Test
    fun testDetailLoading() {
        val state = viewModel.detailState
        whenever(dataRepositoryMock.fetchRepoDetails(fakeRepo)).thenReturn(Flowable.just(fakeRepo)
                .delay(100, TimeUnit.MILLISECONDS))
        viewModel.selectRepo(fakeRepo)
        Assert.assertEquals(true, state.value?.loading)
        Assert.assertNull(state.value?.error)
        Assert.assertNotNull(state.value?.repo)
        Thread.sleep(150)
        Assert.assertEquals(false, state.value?.loading)
        Assert.assertNull(state.value?.error)
        Assert.assertEquals(fakeRepo, state.value?.repo)
    }
}