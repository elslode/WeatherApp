package com.elslode.weather.presentation.mainActivity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.elslode.weather.R
import com.elslode.weather.WeatherApp
import com.elslode.weather.presentation.Screens
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    private val component by lazy {
        (application as WeatherApp).component
    }

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var navigatorHolder: NavigatorHolder
    private val navigator: Navigator

    init {
        navigator = SupportAppNavigator(this, R.id.main_container)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        component.inject(this)
        router.newRootScreen(Screens.MainFragment())
    }

    override fun onResumeFragments() {
        navigatorHolder.setNavigator(navigator)
        super.onResumeFragments()
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }
}