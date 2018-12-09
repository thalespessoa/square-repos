package com.square.repos.view

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import com.square.repos.R
import com.square.repos.RepoDetailBinding
import com.square.repos.app.ViewModelFactory
import com.square.repos.viewmodel.ReposViewModel
import kotlinx.android.synthetic.main.fragment_detail_repo.*

/**
 * Repository detail screen
 *
 * @see ReposViewModel
 * @see DetailRepoState
 *
 * Created by thalespessoa on 9/12/18.
 */
class RepoDetailFragment : Fragment() {

    private val warning: TextView by lazy { tv_warning }
    private val saveButton: ImageButton by lazy { bt_save }
    private val recyclerView: RecyclerView by lazy { recycler_view }
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
        recyclerView.adapter = UsersAdapter()
        saveButton.setOnClickListener(onClickSave)
        warning.setOnClickListener(onClickRefresh)
    }

    //----------------------------------------------------------------------------------------------
    // Actions
    //----------------------------------------------------------------------------------------------

    private val onClickSave = View.OnClickListener {
        reposViewModel?.saveRepo()
    }

    private val onClickRefresh = View.OnClickListener {
        reposViewModel?.detailState?.value?.repo?.let { repo ->
            reposViewModel?.selectRepo(repo)
        }
    }
}