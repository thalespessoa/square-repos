package com.square.repos.app

import com.square.repos.viewmodel.ReposViewModel
import dagger.Component
import javax.inject.Singleton


/**
 * Dagger component to handle dependency injection
 *
 */

@Singleton
@Component(modules = [AppModule::class])
interface ApplicationComponent {
    fun inject(viewModel: ReposViewModel)

    interface Injectable {
        fun inject(applicationComponent: ApplicationComponent)
    }
}