package com.elslode.weather.presentation.mainFragment

import android.app.Application
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.elslode.weather.R
import com.elslode.weather.WeatherApp
import com.elslode.weather.data.sharedPref.HelperPreferences.exist
import com.elslode.weather.data.sharedPref.HelperPreferences.getTemperature
import com.elslode.weather.data.sharedPref.HelperPreferences.getValue
import com.elslode.weather.data.sharedPref.HelperPreferences.put
import com.elslode.weather.data.sharedPref.PrefKeys
import com.elslode.weather.data.sharedPref.PrefKeys.CITY_KEY
import com.elslode.weather.databinding.FragmentMainBinding
import com.elslode.weather.presentation.Screens
import com.elslode.weather.presentation.ViewModelFactory
import com.elslode.weather.presentation.mainActivity.MainActivity
import com.elslode.weather.presentation.mainFragment.adapterRv.WeatherAdapter
import com.elslode.weather.utils.State
import com.squareup.picasso.Picasso
import kotlinx.coroutines.flow.collectLatest
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding: FragmentMainBinding
        get() = _binding ?: throw RuntimeException("FragmentMainBinding == null")

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var _mainViewModel: MainViewModel

    private val component by lazy {
        (requireActivity().application as WeatherApp).component
    }

    @Inject
    lateinit var router: Router
    @Inject
    lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
        setHasOptionsMenu(true)
    }

    private val adapter by lazy {
        WeatherAdapter(activity?.applicationContext as Application, preferences)
    }

    private val latLon by lazy {
        String.format(
            "%sf,%f",
            preferences.getValue(PrefKeys.LATITUDE) as Float,
            preferences.getValue(PrefKeys.LONGITUDE) as Float
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        _mainViewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
        binding.rvWeatherWeek.adapter = adapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity?)?.supportActionBar?.setDisplayHomeAsUpEnabled(false)

        lifecycleScope.launchWhenResumed {
            if (preferences.exist<String>(CITY_KEY)) {
                _mainViewModel.getWeather(preferences.getValue(CITY_KEY) as String)
            } else {
                _mainViewModel.getWeather(latLon)
            }
        }

        adapter.onWeatherItemClickListener = { weather, position ->
            with(router) {
                navigateTo(
                    Screens.DetailFragment(
                        dataTime = weather.date.toString(),
                        q = preferences.getValue(CITY_KEY),
                        position = position
                    )
                )
            }
        }

        lifecycleScope.launchWhenResumed {
            _mainViewModel.weatherSharedFlow.collectLatest {
                when (it.status) {
                    State.LOADING -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    State.ERROR -> {
                        binding.apply {
                            progressBar.visibility = View.GONE
                            rvWeatherWeek.isVisible = false
                            tvChooseOtherCity.isVisible = true
                            tvChooseOtherCity.text = requireActivity().application.getString(R.string.internet_not_unav)
                        }
                    }
                    State.SUCCESS -> {
                        binding.progressBar.visibility = View.GONE
                        adapter.submitList(it.data?.data?.weather)
                        it.data?.data?.current_condition?.last().apply {
                            binding.tvWeatherStateToday.text = this?.weatherDesc?.last()?.value
                            Picasso.get().load(this?.weatherIconUrl?.last()?.value)
                                .into(binding.ivStateWeatherToday)
                            binding.tvWeatherTempToday.text =
                                when (preferences.getTemperature()) {
                                    PrefKeys.c -> this?.FeelsLikeC?.plus(preferences.getTemperature())
                                    PrefKeys.f -> this?.FeelsLikeF?.plus(preferences.getTemperature())
                                    else -> ""
                                }
                        }
                        (requireActivity() as MainActivity).supportActionBar?.title =
                            it.data?.data?.request?.last()?.query
                        preferences.put(CITY_KEY, it.data?.data?.request?.last()?.query)
                    }
                    State.EMPTY -> {
                        binding.apply {
                            progressBar.visibility = View.GONE
                            rvWeatherWeek.isVisible = false
                            binding.tvWeatherTempToday.isVisible = false
                            binding.tvWeatherStateToday.isVisible = false
                            binding.cityName.isVisible = false
                            binding.ivStateWeatherToday.isVisible = false
                            binding.tvChooseOtherCity.isVisible = true
                        }
                        (requireActivity() as MainActivity).supportActionBar?.title =
                            "choose other city!"
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_toolbar, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                router.navigateTo(Screens.SettingsFragment())
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            MainFragment().apply {
                arguments = Bundle().apply {}
            }
    }
}