package com.example.myweatherapp.ui.fragments

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.local.room.FavoriteCityWeatherEntity
import com.example.myweatherapp.ui.HomeViewModel
import com.example.location.utils.LocationResource
import com.example.myweatherapp.R
import com.example.myweatherapp.adapter.ForecastAdapter
import com.example.myweatherapp.databinding.FragmentHomeBinding
import com.example.myweatherapp.snack
import com.example.remote.model.WeatherAndForecastResponseData
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {
    private val viewModel: HomeViewModel by viewModels()
    val args: HomeFragmentArgs by navArgs()

    @Inject
    lateinit var glideRequestManager: RequestManager
    private val _adapter by lazy { ForecastAdapter(glideRequestManager) }
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var _weatherAndForecastResponseData: WeatherAndForecastResponseData? = null

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            viewModel.getLocation()
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)
        binding.forecastRv.adapter = _adapter
        setupHomeView()
        handleCityWeatherSearch()
        navigateToFavoriteWeatherListFragment()
        handleFavoriteCityPersistentData()
        handleFavoriteArgument()
    }

    private fun handleFavoriteArgument() {
        val cityName = args.cityName
        if (cityName.isNotEmpty())
            viewModel.getWeatherAndForecastBasedOnLocation(cityName)
        else
            setupLocationBasedWeatherAndForecastData()
    }

    private fun handleFavoriteCityPersistentData() {
        binding.favButton.setOnClickListener {
            _weatherAndForecastResponseData?.let {
                val favoriteCityWeatherEntity = FavoriteCityWeatherEntity(
                    cityName = it.location.region,
                    conditionIcon = "https:${it.current.condition.icon}",
                    conditionText = it.current.condition.text,
                    tempInCelsius = String.format(
                        resources.getString(R.string.weather_placeholder),
                        it.current.tempC
                    ),
                    tempInFahrenheit = String.format(
                        resources.getString(R.string.weather_placeholder),
                        it.current.tempF
                    )
                )
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
                viewModel.getWeatherAndForecastBasedOnLocation(cityName)
        }
    }

    private fun setupHomeView() {
        lifecycleScope.launchWhenStarted {
            viewModel.weatherAndForecastData.observe(viewLifecycleOwner) {
                _weatherAndForecastResponseData = it
                _weatherAndForecastResponseData?.let { data ->
                    setupImage("https:${data.current.condition.icon}")
                    setupWeatherInfoInUI(data)
                }
            }
        }
    }

    private fun setupLocationBasedWeatherAndForecastData() {
        lifecycleScope.launchWhenStarted {
            viewModel.locationStatus.observe(viewLifecycleOwner) {

                when (it) {
                    is LocationResource.Success -> {
                        viewModel.getWeatherAndForecastBasedOnLocation("${it.data?.latitude},${it.data?.longitude}")
                    }
                    is LocationResource.LocationStatus -> {
                        if (!it.isLocationEnabled)
                            navigateTheUserToLocationSettings()
                    }
                    is LocationResource.PermissionStatus -> {
                        if (!it.IsPermissionGranted)
                            requestPermissions()
                    }
                }
            }
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 0) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                viewModel.getLocation()
            }
        }
    }

    private fun navigateTheUserToLocationSettings() {
        binding.root.snack("Please turn on location", Snackbar.LENGTH_LONG)
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(intent)
    }

    private fun requestPermissions() {
        requestPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
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
                weatherAndForecastDataData.current.tempC
            )
            condition.text = weatherAndForecastDataData.current.condition.text
            city.text = weatherAndForecastDataData.location.region
            precipitationAmount.text = String.format(
                resources.getString(R.string.precipitation_placeholder),
                weatherAndForecastDataData.current.precipIn
            )
            windAmount.text = String.format(
                resources.getString(R.string.wind_speed_placeholder),
                weatherAndForecastDataData.current.windMph
            )
            humidityAmount.text = String.format(
                resources.getString(R.string.humidity_placeholder),
                weatherAndForecastDataData.current.humidity
            )
            visibilityAmount.text = String.format(
                resources.getString(R.string.visibility_placeholder),
                weatherAndForecastDataData.current.visKm
            )
        }
        _adapter.submitList(weatherAndForecastDataData.forecast.forecastday)
    }
}