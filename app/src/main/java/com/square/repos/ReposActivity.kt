package com.square.repos

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class ReposActivity : AppCompatActivity(){

    private val TAG_LIST = "list"
    private val TAG_DETAIL = "detail"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repos)

        supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_list, RepoListFragment(), TAG_LIST)
                .commitAllowingStateLoss()
    }
}
