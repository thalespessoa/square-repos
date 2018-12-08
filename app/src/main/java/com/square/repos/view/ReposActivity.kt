package com.square.repos.view

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v4.app.ListFragment
import android.support.v7.app.AppCompatActivity
import android.util.AttributeSet
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import com.square.repos.R
import com.square.repos.app.ViewModelFactory
import com.square.repos.viewmodel.DetailRepoState
import com.square.repos.viewmodel.ReposViewModel

class ReposActivity : AppCompatActivity(),
        Observer<DetailRepoState>,
        FragmentManager.OnBackStackChangedListener {

    companion object {
        private const val TAG_DETAIL = "detail"
        private const val TAG_LIST = "list"
    }


    private val reposViewModel: ReposViewModel by lazy {
        ViewModelProviders.of(this, ViewModelFactory()).get(ReposViewModel::class.java)
    }

    //----------------------------------------------------------------------------------------------
    // Lifecycle
    //----------------------------------------------------------------------------------------------

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(null)
        setContentView(R.layout.activity_repos)
        reposViewModel.detailState.observe(this, this)
        supportFragmentManager.addOnBackStackChangedListener(this)

        if (supportFragmentManager.findFragmentByTag(TAG_LIST) == null) {
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.fragment_list, RepoListFragment(), TAG_LIST)
                    .commitAllowingStateLoss()
        }
        onBackStackChanged()

        if (findViewById<FrameLayout>(R.id.fragment_details) != null) { // if is tablet or land, shows the screen splitted
            if (supportFragmentManager.findFragmentByTag(TAG_DETAIL) == null) {
                supportFragmentManager
                        .beginTransaction()
                        .add(R.id.fragment_details, RepoDetailFragment(), TAG_DETAIL)
                        .commitAllowingStateLoss()
            }
        }
    }

    //----------------------------------------------------------------------------------------------
    // Navigation (Only to phone portrait)
    //----------------------------------------------------------------------------------------------

    override fun onChanged(viewState: DetailRepoState?) {
        if (supportFragmentManager.findFragmentByTag(TAG_DETAIL) == null && viewState?.repo != null) {
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.fragment_list, RepoDetailFragment(), TAG_DETAIL)
                    .addToBackStack(viewState.repo.name)
                    .commitAllowingStateLoss()
        }
    }

    override fun onBackStackChanged() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = supportFragmentManager
                    .getBackStackEntryAt(supportFragmentManager.backStackEntryCount - 1).name
        } else {
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
            supportActionBar?.setTitle(R.string.app_name)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return true
    }
}
