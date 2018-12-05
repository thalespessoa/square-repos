package com.square.repos.view

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.square.repos.R
import com.square.repos.RepoDetailBinding
import com.square.repos.app.ViewModelFactory
import com.square.repos.viewmodel.ReposViewModel
import kotlinx.android.synthetic.main.fragment_detail_repo.*

class RepoDetailFragment : Fragment() {

    private val saveButton: Button by lazy { bt_save }
    private val recyclerView: RecyclerView by lazy { recycler_view }
    private val usersAdapter: UsersAdapter by lazy { UsersAdapter() }
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
            DataBindingUtil.inflate<RepoDetailBinding>(
                    inflater, R.layout.fragment_detail_repo, container, false)
                    .apply {
                        setLifecycleOwner(this@RepoDetailFragment)
                        viewModel = reposViewModel
                    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.adapter = usersAdapter
        saveButton.setOnClickListener {
            reposViewModel?.saveRepo()
        }
    }

}