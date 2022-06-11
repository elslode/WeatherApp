package com.elslode.weather.di

import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
interface NavigateModule {

    val cicerone: Cicerone<Router>
        get() = Cicerone.create()


        @Provides
        @Singleton
        fun provideRouter(): Router = cicerone.router

        @Provides
        @Singleton
        fun provideNavigatorHolder(): NavigatorHolder {
            return cicerone.getNavigatorHolder()
        }

}