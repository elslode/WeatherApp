package com.elslode.weather.di

import android.app.Application
import com.elslode.weather.presentation.detailFragment.DetailFragment
import com.elslode.weather.presentation.mainActivity.MainActivity
import com.elslode.weather.presentation.mainFragment.MainFragment
import com.elslode.weather.presentation.settings.SettingFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Component(
    modules = [
        ViewModelModule::class,
        CiceroneModule::class,
        DataModule::class
    ]
)
@Singleton
@ApplicationScope
interface ApplicationComponent {

    fun inject(mainFragment: MainFragment)
    fun inject(mainActivity: MainActivity)
    fun inject(settingFragment: SettingFragment)
    fun inject(detailFragment: DetailFragment)

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance application: Application
        ): ApplicationComponent
    }
}