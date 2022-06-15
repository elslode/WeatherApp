package com.elslode.weather.presentation.mainFragment

import android.app.Application
import android.os.Bundle
import android.view.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.elslode.weather.R
import com.elslode.weather.WeatherApp
import com.elslode.weather.databinding.FragmentMainBinding
import com.elslode.weather.presentation.ViewModelFactory
import com.elslode.weather.presentation.mainFragment.adapterRv.WeatherAdapter
import com.elslode.weather.utils.PrefHelper
import com.elslode.weather.utils.PrefKeys
import com.elslode.weather.utils.State
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding: FragmentMainBinding
        get() = _binding ?: throw RuntimeException("FragmentMainBinding == null")

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var _mainViewModel: MainViewModel

    private val prefHelper by lazy {
        PrefHelper(requireContext())
    }

    private val component by lazy {
        (requireActivity().application as WeatherApp).component
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
        setHasOptionsMenu(true)
    }

    private val adapter by lazy {
        WeatherAdapter(activity?.applicationContext as Application)
    }

    private val latLon by lazy {
        String.format(
            "%f,%f",
            prefHelper.getFloat(PrefKeys.LATITUDE),
            prefHelper.getFloat(PrefKeys.LONGITUDE)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        _mainViewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
        binding.rvWeatherWeek.adapter = adapter

        CoroutineScope(Dispatchers.IO).launch {
            _mainViewModel.getWeather(latLon)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenStarted {
            _mainViewModel.weatherSharedFlow.collectLatest {
                when (it.status) {
                    State.LOADING -> {
                        binding.apply {
                            progressBar.isVisible = true
                            internetInclude.textView.isVisible = false
                            internetInclude.imageView2.isVisible = false
                        }
                    }
                    State.ERROR -> {
                        binding.apply {
                            progressBar.isVisible = false
                            internetInclude.textView.isVisible = true
                            internetInclude.imageView2.isVisible = true
                        }
                    }
                    State.SUCCESS -> {
                        adapter.submitList(it.data?.data?.weather)
                        binding.apply {
                            progressBar.isVisible = false
                            internetInclude.textView.isVisible = false
                            internetInclude.imageView2.isVisible = false
                        }
                        it.data?.data?.current_condition?.apply {
                            this.last().apply {
                                binding.tvWeatherStateToday.text = this.weatherDesc?.last()?.value
                                Picasso.get().load(this.weatherIconUrl?.last()?.value)
                                    .into(binding.ivStateWeatherToday)
                                binding.tvWeatherTempToday.text = requireActivity().getString(R.string.temp).format(this.FeelsLikeC)
                            }
                        }
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