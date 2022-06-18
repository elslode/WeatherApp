package com.elslode.weather.presentation

import androidx.fragment.app.Fragment
import com.elslode.weather.presentation.detailFragment.DetailFragment
import com.elslode.weather.presentation.settings.SettingFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

class Screens {

    class MainFragment : SupportAppScreen() {
        override fun getFragment(): Fragment =
            com.elslode.weather.presentation.mainFragment.MainFragment()
    }

    class SettingsFragment() : SupportAppScreen() {
        override fun getFragment(): Fragment =
            SettingFragment.newInstance()
    }

    class DetailFragment(val q: String, val dataTime: String, val position: Int) : SupportAppScreen() {
        override fun getFragment(): Fragment =
            com.elslode.weather.presentation
                .detailFragment
                .DetailFragment
                .newInstance(
                q = q,
                dataTime = dataTime,
                    position = position
            )
    }
}