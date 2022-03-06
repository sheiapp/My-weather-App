package com.example.myweatherapp.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.RequestManager
import com.example.myweatherapp.ui.FavoriteCitiesViewModel
import com.example.myweatherapp.R
import com.example.myweatherapp.adapter.FavoriteCitiesAdapter
import com.example.myweatherapp.databinding.FragmentFavoriteCitiesBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FavoriteCitiesFragment : Fragment(R.layout.fragment_favorite_cities) {
    private val viewModel: FavoriteCitiesViewModel by viewModels()

    @Inject
    lateinit var glideRequestManager: RequestManager
    private val _adapter by lazy { FavoriteCitiesAdapter(glideRequestManager){cityName->
        navigateToHome(cityName)
    } }
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
        viewModel.weatherAndForecastData.observe(viewLifecycleOwner) {
            it?.let {
                _adapter.submitList(it)
            }
        }
    }

    private fun navigateToHome(cityName:String){
        val goBackToHome=FavoriteCitiesFragmentDirections.actionNavigateToHomeFragment()
        goBackToHome.cityName=cityName
        findNavController().navigate(goBackToHome)
    }


}