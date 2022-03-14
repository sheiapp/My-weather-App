package com.example.myweatherapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.local.room.WeatherEntity
import com.example.myweatherapp.databinding.FavCityItemBinding

/**
 * Created by Shaheer cs on 05/03/2022.
 */
class FavoriteCitiesAdapter(private val requestManager: RequestManager,private val clickEventData:(String?) ->Unit) :
    ListAdapter<WeatherEntity, FavoriteCitiesItemViewHolder>(FavoriteCitiesDiffUtil()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoriteCitiesItemViewHolder {
        val itemView =
            FavCityItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteCitiesItemViewHolder(itemView, requestManager)
    }

    override fun onBindViewHolder(holder: FavoriteCitiesItemViewHolder, position: Int) {
        holder.onBindView(getItem(position))
        holder.itemView.setOnClickListener {
            clickEventData(getItem(position).cityName)
        }
    }
}

class FavoriteCitiesItemViewHolder(
    private val viewItem: FavCityItemBinding,
    private val requestManager: RequestManager
) :
    RecyclerView.ViewHolder(viewItem.root) {
    fun onBindView(dataItem: WeatherEntity) {
        viewItem.cityName.text = dataItem.cityName
        viewItem.temperature.text = dataItem.tempInCelsius
        viewItem.condition.text = dataItem.conditionText
        setupImage(dataItem.conditionIcon)
    }

    private fun setupImage(imageUrl: String?) {
        requestManager
            .load(imageUrl)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(viewItem.img)
    }
}

class FavoriteCitiesDiffUtil : DiffUtil.ItemCallback<WeatherEntity>() {
    override fun areItemsTheSame(
        oldItem: WeatherEntity,
        newItem: WeatherEntity
    ) =
        newItem.columnId == oldItem.columnId

    override fun areContentsTheSame(
        oldItem: WeatherEntity,
        newItem: WeatherEntity
    ) =
        newItem == oldItem
}