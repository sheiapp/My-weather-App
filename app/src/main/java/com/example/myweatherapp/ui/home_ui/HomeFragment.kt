package com.example.myweatherapp.ui.home_ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.local.room.FavoriteCityWeatherEntity
import com.example.myweatherapp.R
import com.example.myweatherapp.adapter.ForecastAdapter
import com.example.myweatherapp.databinding.FragmentHomeBinding
import com.example.myweatherapp.ui.showSnackBar
import com.example.remote.model.WeatherAndForecastResponseData
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
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
    private var _weatherAndForecastResponseData: WeatherAndForecastResponseData? = null

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)
        binding.forecastRv.adapter = _adapter
        showMessageToUser()
        handleLoadingState()
        setupHomeView()
        handleCityWeatherSearch()
        navigateToFavoriteWeatherListFragment()
        handleFavoriteCityPersistentData()
        handleFavoriteArgument()
    }

    private fun showMessageToUser() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.message.collectLatest {
                    binding.root.showSnackBar(it, Snackbar.LENGTH_LONG)
                }
            }
        }
    }

    private fun handleLoadingState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isLoading.collectLatest {
                    binding.progressBar.visibility = if (it) View.VISIBLE else View.INVISIBLE
                }
            }
        }
    }


    private fun handleFavoriteArgument() {
        val cityName = args.cityName
        if (cityName.isNotEmpty())
            viewModel.getWeatherAndForecastBasedOnCityName(cityName)
        else
            viewModel.getWeatherAndForecastBasedOnLocation()
    }

    private fun handleFavoriteCityPersistentData() {
        binding.favButton.setOnClickListener {
            _weatherAndForecastResponseData?.let {
                val favoriteCityWeatherEntity = FavoriteCityWeatherEntity(
                    cityName = it.location?.region,
                    conditionIcon = "https:${it.current?.condition?.icon}",
                    conditionText = it.current?.condition?.text,
                    tempInCelsius = String.format(
                        resources.getString(R.string.weather_placeholder),
                        it.current?.tempC
                    ),
                    tempInFahrenheit = String.format(
                        resources.getString(R.string.weather_placeholder),
                        it.current?.tempF
                    )
                )
                binding.root.showSnackBar(getString(R.string.favorite_msg), Snackbar.LENGTH_LONG)
                viewModel.persistTheCityWeatherData(favoriteCityWeatherEntity)
            }
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
                viewModel.getWeatherAndForecastBasedOnCityName(cityName)
        }
    }

    private fun setupHomeView() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.weatherAndForecastData.collectLatest {
                    if (it.current != null && it.location != null) {
                        _weatherAndForecastResponseData = it
                        _weatherAndForecastResponseData?.let { data ->
                            setupImage("https:${data.current?.condition?.icon}")
                            setupWeatherInfoInUI(data)
                        }
                    }
                }
            }
        }
    }

    private fun setupImage(imageUrl: String?) {
        glideRequestManager
            .load(imageUrl)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.weatherImg)
    }

    private fun setupWeatherInfoInUI(weatherAndForecastDataData: WeatherAndForecastResponseData) {
        binding.apply {
            temperature.text = String.format(
                resources.getString(R.string.weather_placeholder),
                weatherAndForecastDataData.current?.tempC
            )
            condition.text = weatherAndForecastDataData.current?.condition?.text
            city.text = weatherAndForecastDataData.location?.region
            precipitationAmount.text = String.format(
                resources.getString(R.string.precipitation_placeholder),
                weatherAndForecastDataData.current?.precipIn
            )
            windAmount.text = String.format(
                resources.getString(R.string.wind_speed_placeholder),
                weatherAndForecastDataData.current?.windMph
            )
            humidityAmount.text = String.format(
                resources.getString(R.string.humidity_placeholder),
                weatherAndForecastDataData.current?.humidity
            )
            visibilityAmount.text = String.format(
                resources.getString(R.string.visibility_placeholder),
                weatherAndForecastDataData.current?.visKm
            )
        }
        _adapter.submitList(weatherAndForecastDataData.forecast?.forecastday)
    }
}