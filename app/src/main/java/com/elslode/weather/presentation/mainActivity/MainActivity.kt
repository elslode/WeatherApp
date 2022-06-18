package com.elslode.weather.presentation.mainActivity

import android.content.Intent
import android.location.Location
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.elslode.weather.R
import com.elslode.weather.WeatherApp
import com.elslode.weather.data.sharedPref.PrefHelper
import com.elslode.weather.data.sharedPref.PrefKeys
import com.elslode.weather.presentation.Screens
import mumayank.com.airlocationlibrary.AirLocation
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import ru.terrakok.cicerone.commands.Back
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
    @Inject
    lateinit var prefHelper: PrefHelper

    private val airLocation = AirLocation(
        this, object : AirLocation.Callback {
            override fun onSuccess(locations: ArrayList<Location>) {
                locations.map {
                    prefHelper.put(PrefKeys.LATITUDE, it.latitude.toFloat())
                    prefHelper.put(PrefKeys.LONGITUDE, it.longitude.toFloat())
                }
            }

            override fun onFailure(locationFailedEnum: AirLocation.LocationFailedEnum) {
                prefHelper.put(PrefKeys.LATITUDE, PrefKeys.LATITUDE_FLOAT)
                prefHelper.put(PrefKeys.LONGITUDE, PrefKeys.LONGITUDE_FLOAT)
            }
        },
        true
    )

    override fun onStart() {
        super.onStart()
        airLocation.start()
    }

    init {
        navigator = SupportAppNavigator(this, R.id.main_container)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        component.inject(this)
        router.newRootScreen(Screens.MainFragment())

        if (!prefHelper.exists(PrefKeys.TEMPERATURE)) {
            prefHelper.put(PrefKeys.TEMPERATURE, R.id.radioButtonC)
        }
    }

    override fun onResumeFragments() {
        navigatorHolder.setNavigator(navigator)
        super.onResumeFragments()
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        airLocation.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        airLocation.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Back()
    }
}