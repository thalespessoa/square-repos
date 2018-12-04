package com.square.repos.app

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider

class ViewModelFactory : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val t = super.create(modelClass)
        if (t is ApplicationComponent.Injectable) {
            (t as ApplicationComponent.Injectable).inject(ApplicationController.graph)
        }
        return t
    }
}