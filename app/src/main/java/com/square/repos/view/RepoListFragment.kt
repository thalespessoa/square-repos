package com.square.repos.view

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.square.repos.R
import com.square.repos.RepoListBinding
import com.square.repos.app.ViewModelFactory
import com.square.repos.model.Repo
import com.square.repos.viewmodel.ReposViewModel
import kotlinx.android.synthetic.main.fragment_list_repo.*
import kotlinx.android.synthetic.main.view_warning.view.*

class RepoListFragment : Fragment(), ReposAdapter.OnSelectRepo {

    private val recyclerView: RecyclerView by lazy { recycler_view }
    private val reposAdapter: ReposAdapter by lazy { ReposAdapter(this) }
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
            DataBindingUtil.inflate<RepoListBinding>(
                    inflater, R.layout.fragment_list_repo, container, false)
                    .apply {
                        setLifecycleOwner(this@RepoListFragment)
                        viewModel = reposViewModel
                        warning.title.text = reposViewModel?.detailState?.value?.error?.message
                    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.adapter = reposAdapter
    }

    //----------------------------------------------------------------------------------------------
    // Actions
    //----------------------------------------------------------------------------------------------

    override fun onSelectRepo(repo: Repo) {
        reposViewModel?.selectRepo(repo)
        Toast.makeText(context, "Select: ${repo.name}", Toast.LENGTH_SHORT).show()
    }
}