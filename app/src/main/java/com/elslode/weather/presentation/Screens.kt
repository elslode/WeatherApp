package com.elslode.weather.presentation

import androidx.fragment.app.Fragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

class Screens {

    class MainFragment : SupportAppScreen() {
        override fun getFragment(): Fragment =
            com.elslode.weather.presentation.mainFragment.MainFragment.newInstance()
    }
}