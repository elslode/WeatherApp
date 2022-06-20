package com.elslode.weather.presentation.settings

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
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
import com.elslode.weather.data.sharedPref.HelperPreferences.getValue
import com.elslode.weather.data.sharedPref.HelperPreferences.put
import com.elslode.weather.data.sharedPref.PrefKeys
import com.elslode.weather.databinding.FragmentSettingBinding
import com.elslode.weather.presentation.ViewModelFactory
import com.elslode.weather.presentation.mainActivity.MainActivity
import com.elslode.weather.presentation.mainFragment.MainViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class SettingFragment : Fragment() {

    private var _binding: FragmentSettingBinding? = null
    private val binding: FragmentSettingBinding
        get() = _binding ?: throw RuntimeException("FragmentSettingBinding == null")

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var _mainVM: MainViewModel

    @Inject
    lateinit var preferences: SharedPreferences

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
        _mainVM = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).supportActionBar?.title = requireActivity().getString(R.string.settings_title)

        binding.apply {
            rgTemperature.check(preferences.getValue(PrefKeys.TEMPERATURE))
            queryEditText.doAfterTextChanged {
                it?.trim()
            }
            searchButton.setOnClickListener {
                _mainVM.searchCity(queryEditText.text?.toString())
                hideKeyboard()
                exit()
            }
            openWebSite.setOnClickListener {
                openWebsite()
            }
        }

        lifecycleScope.launchWhenCreated {
            _mainVM.searchIsNotFoundSharedFlow.collectLatest {
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
        preferences.put(
            PrefKeys.TEMPERATURE,
            binding.rgTemperature.checkedRadioButtonId
        )
        router.exit()
    }

    private fun openWebsite(){
        val address: Uri = Uri.parse("https://yandex.ru/pogoda/saint-petersburg?lat=59.833432&lon=30.349402")
        val openLinkIntent = Intent(Intent.ACTION_VIEW, address)

        if (openLinkIntent.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(openLinkIntent)
        } else {
            Snackbar.make(requireView(), "Sorry, link is null", Snackbar.LENGTH_SHORT).show()
        }
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