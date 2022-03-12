package com.example.myweatherapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.local.model.Forecast
import com.example.myweatherapp.databinding.ForecastItemBinding
import kotlinx.android.synthetic.main.forecast_item.view.*

/**
 * Created by Shaheer cs on 04/03/2022.
 */
class ForecastAdapter(private val requestManager: RequestManager) :
    ListAdapter<Forecast, ForecastItemViewHolder>(ForeCastDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastItemViewHolder {
        val itemView =
            ForecastItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ForecastItemViewHolder(itemView, requestManager)
    }

    override fun onBindViewHolder(holder: ForecastItemViewHolder, position: Int) {
        holder.onBindView(getItem(position))
    }
}

class ForecastItemViewHolder(
    itemView: ForecastItemBinding,
    private val requestManager: RequestManager
) :
    RecyclerView.ViewHolder(itemView.root) {
    fun onBindView(dataItem: Forecast) {
        itemView.date.text = dataItem.date
        itemView.condition.text = dataItem.conditionText
        setupImage("https:${dataItem.conditionIcon}")
    }

    private fun setupImage(imageUrl: String?) {
        requestManager
            .load(imageUrl)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(itemView.icon)
    }
}

class ForeCastDiffUtil : DiffUtil.ItemCallback<Forecast>() {
    override fun areItemsTheSame(oldItem: Forecast, newItem: Forecast) =
        newItem.date == oldItem.date

    override fun areContentsTheSame(oldItem: Forecast, newItem: Forecast) =
        newItem == oldItem
}