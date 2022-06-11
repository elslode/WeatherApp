package com.elslode.weather.di

import android.app.Application
import com.elslode.weather.presentation.mainActivity.MainActivity
import com.elslode.weather.presentation.mainFragment.MainFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Component(
    modules = [
        ApiModule::class,
        ViewModelModule::class,
        CiceroneModule::class
    ]
)
@Singleton
@ApplicationScope
interface ApplicationComponent {

    fun inject(mainFragment: MainFragment)
    fun inject(mainActivity: MainActivity)

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance application: Application
        ): ApplicationComponent
    }
}