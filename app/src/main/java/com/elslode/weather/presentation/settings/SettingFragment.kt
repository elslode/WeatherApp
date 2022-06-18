package com.elslode.weather.presentation.settings

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.elslode.weather.R
import com.elslode.weather.WeatherApp
import com.elslode.weather.data.sharedPref.PrefHelper
import com.elslode.weather.data.sharedPref.PrefKeys
import com.elslode.weather.databinding.FragmentSettingBinding
import com.elslode.weather.presentation.Screens
import com.elslode.weather.presentation.ViewModelFactory
import com.elslode.weather.presentation.mainActivity.MainActivity
import com.elslode.weather.presentation.mainFragment.MainViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import ru.terrakok.cicerone.Router
import javax.inject.Inject


class SettingFragment : Fragment() {

    private var _binding: FragmentSettingBinding? = null
    private val binding: FragmentSettingBinding
        get() = _binding ?: throw RuntimeException("FragmentSettingBinding == null")

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var _settingVM: MainViewModel

    @Inject
    lateinit var prefHelper: PrefHelper

    @Inject
    lateinit var router: Router

    private val component by lazy {
        (requireActivity().application as WeatherApp).component
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as MainActivity?)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        activity?.actionBar?.title = requireActivity().getString(R.string.settings_title)
        setHasOptionsMenu(true)
        component.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        _settingVM = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).supportActionBar?.title = requireActivity().getString(R.string.settings_title)

        binding.apply {
            rgTemperature.check(prefHelper.getInt(PrefKeys.TEMPERATURE))
            queryEditText.doAfterTextChanged {
                it?.trim()
            }
            searchButton.setOnClickListener {
                _settingVM.searchCity(queryEditText.text?.toString())
                hideKeyboard()
                exit()
            }
        }

        lifecycleScope.launchWhenCreated {
            _settingVM.searchIsNotFoundSharedFlow.collectLatest {
                Snackbar.make(requireView(), "City is empty", Snackbar.LENGTH_SHORT).show()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    exit()
                }
            })


    }

    private fun hideKeyboard() {
        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    private fun exit() {
        prefHelper.put(
            PrefKeys.TEMPERATURE,
            binding.rgTemperature.checkedRadioButtonId
        )
        router.exit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) exit()
        return true
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            SettingFragment().apply {
                arguments = Bundle().apply {}
            }
    }
}