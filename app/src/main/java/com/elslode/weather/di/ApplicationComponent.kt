package com.elslode.weather.di

import android.app.Application
import com.elslode.weather.presentation.MainActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Component(
    modules = [
        NavigateModule::class,
        ApiModule::class,
        ViewModelModule::class
    ]
)

@Singleton
interface ApplicationComponent {

    fun inject(mainActivity: MainActivity)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance application: Application
        ): ApplicationComponent
    }
}