package com.elslode.weather.presentation.detailFragment

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.elslode.weather.R
import com.elslode.weather.WeatherApp
import com.elslode.weather.data.sharedPref.PrefHelper
import com.elslode.weather.data.sharedPref.PrefKeys
import com.elslode.weather.databinding.FragmentDetailBinding
import com.elslode.weather.presentation.ViewModelFactory
import com.elslode.weather.presentation.detailFragment.adapterHourlyRv.HourlyAdapter
import com.elslode.weather.presentation.mainActivity.MainActivity
import com.elslode.weather.utils.State
import kotlinx.coroutines.flow.collectLatest
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding: FragmentDetailBinding
        get() = _binding ?: throw RuntimeException("FragmentDetailBinding is null")

    private var query: String? = null
    private var dataTime: String? = null
    private var position: Int = 0

    private val component by lazy {
        (requireActivity().application as WeatherApp).component
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var _detailVM: DetailViewModel

    @Inject
    lateinit var router: Router
    @Inject
    lateinit var prefHelper: PrefHelper
    @Inject
    lateinit var application: Application
    private val adapterHourly by lazy {
        HourlyAdapter(application = application, prefHelper)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        (activity as MainActivity?)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        component.inject(this)
        arguments?.let {
            query = it.getString(QUERY_KEY)
            dataTime = it.getString(DATA_KEY)
            position = it.getInt(POSITION_KEY)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _detailVM.getWeatherDetail(q = query, dataTime = dataTime)
        lifecycleScope.launchWhenStarted {
            _detailVM.detailSharedFlow.collectLatest {
                when(it.status) {
                    State.LOADING -> {
                        binding.pbDetail.isVisible = true
                    }
                    State.ERROR -> {
                        binding.apply {
                            tvTempOfDetailsMain.isVisible = false
                            windSpeed.isVisible = false
                            tvHumidity.isVisible = false
                            weatherDescription.isVisible = false
                            rvHours.isVisible = false
                            tvAvrgTempDesc.isVisible = false
                            tvVisibility.isVisible = false
                        }
                    }
                    State.SUCCESS -> {
                        val currentCondition = it.data?.data?.current_condition?.last()
                        val hourly = it.data?.data?.weather?.last()?.hourly
                        val weather = it.data?.data?.weather
                        (requireActivity() as MainActivity).supportActionBar?.title = dataTime
                        binding.apply {
                            adapterHourly.submitList(hourly)

                            pbDetail.isVisible = false

                            tvHumidity.text = application.getString(R.string.humidity)
                                .format(currentCondition?.humidity).plus(application.getString(R.string.percent))
                            tvVisibility.text = application.getString(R.string.visibility_km)
                                .format(currentCondition?.visibility)

                            tvTempOfDetailsMain.text = when (prefHelper.getTemperature()) {
                                PrefKeys.c ->
                                    application.getString(R.string.temp_c)
                                        .format(weather?.get(position)?.avgtempC)

                                PrefKeys.f ->
                                    application.getString(R.string.temp_f)
                                        .format(weather?.get(position)?.avgtempF)

                                else -> application.getString(R.string.internet_not_unav)
                            }
                            weatherDescription.text = currentCondition?.lang_ru?.last()?.value
                            windSpeed.text = application.getString(R.string.wind_speed_km)
                                .format(currentCondition?.windspeedKmph)

                        }
                    }
                    State.EMPTY -> {
                        binding.apply {
                            tvTempOfDetailsMain.isVisible = false
                            windSpeed.isVisible = false
                            tvHumidity.isVisible = false
                            weatherDescription.isVisible = false
                            rvHours.isVisible = false
                            tvAvrgTempDesc.isVisible = false
                            tvVisibility.isVisible = false
                        }
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        _detailVM = ViewModelProvider(this, viewModelFactory)[DetailViewModel::class.java]
        binding.rvHours.adapter = adapterHourly
        return binding.root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) router.exit()
        return true
    }

    companion object {
        private const val QUERY_KEY = "query"
        private const val DATA_KEY = "data"
        private const val POSITION_KEY = "position"

        @JvmStatic
        fun newInstance(q: String, dataTime: String, position: Int) =
            DetailFragment().apply {
                arguments = Bundle().apply {
                    putString(QUERY_KEY, q)
                    putString(DATA_KEY, dataTime)
                    putInt(POSITION_KEY, position)
                }
            }
    }
}