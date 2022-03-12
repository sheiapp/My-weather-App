package com.example.myweatherapp.ui.home_ui

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.local.room.WeatherEntity
import com.example.myweatherapp.R
import com.example.myweatherapp.adapter.ForecastAdapter
import com.example.myweatherapp.databinding.FragmentHomeBinding
import com.example.myweatherapp.ui.collectLatestLifecycleFlow
import com.example.myweatherapp.ui.showSnackBar
import com.example.remote.util.Resource
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {
    @Inject
    lateinit var glideRequestManager: RequestManager
    private val viewModel: HomeViewModel by viewModels()
    private val args: HomeFragmentArgs by navArgs()
    private val _adapter by lazy { ForecastAdapter(glideRequestManager) }
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)
        binding.forecastRv.adapter = _adapter
        showMessageToUser()
        setupHomeView()
        handleCityWeatherSearch()
        navigateToFavoriteWeatherListFragment()
        handleFavoriteCityPersistentData()
        handleFavoriteArgument()
        handleTempSwitch()
    }

    private fun setupHomeView(){
        collectLatestLifecycleFlow(viewModel.weatherAndForecastDataSet){ weatherData->
            weatherData?.let {result->
                result.data?.let { weatherData->  setupWeatherInfoInUI(weatherData) }
                binding.progressBar.isVisible = result is Resource.Loading && result.data==null
                result.message?.let { it1 -> binding.root.showSnackBar(it1, Snackbar.LENGTH_LONG) }
            }
        }
    }

    private fun handleTempSwitch() {
        collectLatestLifecycleFlow(viewModel.tempUnitState) { isChecked ->
            isChecked?.let {
                binding.tempSwitch.isChecked = it
            }
        }
        binding.tempSwitch.setOnCheckedChangeListener { buttonView, is_checked ->
            viewModel.setTempUnit(is_checked)
        }
    }

    private fun showMessageToUser() {
        collectLatestLifecycleFlow(viewModel.message) {
            binding.root.showSnackBar(it, Snackbar.LENGTH_LONG)
        }
    }

    private fun handleFavoriteArgument() {
        val cityName = args.cityName
        if (cityName.isNotEmpty())
            viewModel.getWeatherAndForecastForCityName(cityName)
        else
            viewModel.getWeatherAndForecastForLocation()
    }

    private fun handleFavoriteCityPersistentData() {
        binding.favButton.setOnClickListener {
            viewModel.setTheCurrentSelectedCityAsFavorite()
        }
    }

    private fun navigateToFavoriteWeatherListFragment() {
        binding.favWeatherListButton.setOnClickListener {
            val gotoFavoriteCitiesFragment =
                HomeFragmentDirections.actionNavigateToFavoriteCitiesFragment()
            findNavController().navigate(gotoFavoriteCitiesFragment)
        }
    }

    private fun handleCityWeatherSearch() {
        binding.searchButton.setOnClickListener {
            val cityName = binding.searchCity.text.toString()
            if (cityName.isNotEmpty())
                viewModel.getWeatherAndForecastForCityName(cityName)
        }
    }

    private fun setupImage(imageUrl: String?) {
        glideRequestManager
            .load(imageUrl)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.weatherImg)
    }

    private fun setupTempUnit(weatherData: WeatherEntity) {
            binding.temperature.text = String.format(
                resources.getString(if (weatherData.isTempInCelsius) R.string.celsius_placeholder else R.string.fahrenheit_placeholder),
                if (weatherData.isTempInCelsius) weatherData.tempInCelsius else weatherData.tempInFahrenheit
            )
    }

    private fun setupWeatherInfoInUI(weatherData: WeatherEntity) {
        setupImage("https:${weatherData.conditionIcon}")
        setupTempUnit(weatherData)
        binding.apply {
            condition.text = weatherData.conditionText
            city.text = weatherData.cityName
            precipitationAmount.text = String.format(
                resources.getString(R.string.precipitation_placeholder),
                weatherData.precipitation
            )
            windAmount.text = String.format(
                resources.getString(R.string.wind_speed_placeholder),
                weatherData.windSpeed
            )
            humidityAmount.text = String.format(
                resources.getString(R.string.humidity_placeholder),
                weatherData.humidity
            )
            visibilityAmount.text = String.format(
                resources.getString(R.string.visibility_placeholder),
                weatherData.visibility
            )
        }
        _adapter.submitList(weatherData.forecast)
    }
}