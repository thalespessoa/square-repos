package com.square.repos.view

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.square.repos.R
import com.square.repos.app.ViewModelFactory
import com.square.repos.viewmodel.DetailRepoState
import com.square.repos.viewmodel.ReposViewModel

class ReposActivity : AppCompatActivity(), Observer<DetailRepoState> {

    companion object {
        private const val TAG_DETAIL = "detail"
    }

    private val reposViewModel: ReposViewModel by lazy {
        ViewModelProviders.of(this, ViewModelFactory()).get(ReposViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repos)

//        reposViewModel.detailState.observe(this, this)
    }

    override fun onChanged(viewState: DetailRepoState?) {
        supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_list, RepoDetailFragment(), TAG_DETAIL)
                .addToBackStack(getString(R.string.title_detail))
                .commitAllowingStateLoss()

        supportFragmentManager.executePendingTransactions()
    }
}
