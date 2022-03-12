package com.example.myweatherapp.ui.favoite_cities_ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.RequestManager
import com.example.myweatherapp.R
import com.example.myweatherapp.adapter.FavoriteCitiesAdapter
import com.example.myweatherapp.databinding.FragmentFavoriteCitiesBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class FavoriteCitiesFragment : Fragment(R.layout.fragment_favorite_cities) {
    private val viewModel: FavoriteCitiesViewModel by viewModels()

    @Inject
    lateinit var glideRequestManager: RequestManager
    private val _adapter by lazy {
        FavoriteCitiesAdapter(glideRequestManager) { cityName ->
            navigateToHome(cityName)
        }
    }
    private var _binding: FragmentFavoriteCitiesBinding? = null
    private val binding get() = _binding!!

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFavoriteCitiesBinding.bind(view)
        binding.favLisRv.adapter = _adapter
        setupFavoriteCitiesUI()
    }

    private fun setupFavoriteCitiesUI() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.weatherAndForecastData.collect {
                    it.let {
                        _adapter.submitList(it)
                    }
                }
            }
        }
    }

    private fun navigateToHome(cityName: String?) {
        val goBackToHome =
            FavoriteCitiesFragmentDirections.actionNavigateToHomeFragment()
        cityName?.let {
            goBackToHome.cityName = it
        }
        findNavController().navigate(goBackToHome)
    }

}