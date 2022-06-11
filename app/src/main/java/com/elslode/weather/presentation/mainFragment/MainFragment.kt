package com.elslode.weather.presentation.mainFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.elslode.weather.WeatherApp
import com.elslode.weather.databinding.FragmentMainBinding
import com.elslode.weather.presentation.ViewModelFactory
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        _mainViewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
        return binding.root
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            MainFragment().apply {
                arguments = Bundle().apply {}
            }
    }
}