package com.square.repos.view

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.square.repos.R
import com.square.repos.RepoListBinding
import com.square.repos.app.ViewModelFactory
import com.square.repos.model.Repo
import com.square.repos.viewmodel.ReposViewModel
import kotlinx.android.synthetic.main.fragment_list_repo.*

class RepoListFragment : Fragment() {

    private val warning: TextView by lazy { tv_warning }
    private val recyclerView: RecyclerView by lazy { recycler_view }
    private val reposAdapter: ReposAdapter by lazy { ReposAdapter(onSelectRepo) }
    private val reposViewModel: ReposViewModel? by lazy {
        activity?.let {
            ViewModelProviders.of(it, ViewModelFactory())
                    .get(ReposViewModel::class.java)
        }
    }

    //----------------------------------------------------------------------------------------------
    // Lifecycle
    //----------------------------------------------------------------------------------------------

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            DataBindingUtil.inflate<RepoListBinding>(inflater, R.layout.fragment_list_repo, container, false)
                    .apply {
                        setLifecycleOwner(this@RepoListFragment)
                        viewModel = reposViewModel
                    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.adapter = reposAdapter
        warning.setOnClickListener(onClickRefresh)
    }

    //----------------------------------------------------------------------------------------------
    // Actions
    //----------------------------------------------------------------------------------------------

    private val onSelectRepo = object : ReposAdapter.OnSelectRepo {
        override fun onSelectRepo(repo: Repo) {
            reposViewModel?.selectRepo(repo)
        }
    }

    private val onClickRefresh = View.OnClickListener {
        reposViewModel?.listRepos()
    }
}