package com.bp.weatherapp.views.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bp.weatherapp.R
import com.bp.weatherapp.databinding.AdapterWeatherListHeaderBinding
import com.bp.weatherapp.databinding.AdapterWeatherListItemBinding
import com.bp.weatherapp.models.Location
import com.bp.weatherapp.models.Weather
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import java.lang.Exception
import java.util.ArrayList

class WeatherListAdapter(val glide: RequestManager) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val VIEWTYPE_HEADER = 0;
    val VIEWTYPE_CONTENT = 1;
    val requestOptions: RequestOptions by lazy {
        RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)
    }

    var list: Array<Pair<Location, Pair<Weather, Weather>>>? = arrayOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemViewType(position: Int): Int {
        if (position == 0) {
            return VIEWTYPE_HEADER
        } else {
            return VIEWTYPE_CONTENT
        }
    }

    override fun getItemId(position: Int): Long {

        try {
            return this.list?.get(position - 1)?.run {
                return@run this.first.woeid.hashCode().toLong()
            }!!
        } catch (e: Exception) {
            return 0
        }
    }

    inner class WeatherListAdapterViewHolder(val binding: AdapterWeatherListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindViewHolder(item: Pair<Location, Pair<Weather, Weather>>) {
            binding.location = item.first
            binding.weatherToday = item.second.first
            binding.weatherTomorrow = item.second.second
            binding.viewHolder = this
            binding.executePendingBindings()
            glide.load(item.second.first.getStateAsResource()).apply(requestOptions)
                .into(binding.info1.image)
            glide.load(item.second.second.getStateAsResource()).apply(requestOptions)
                .into(binding.info2.image)
        }
    }

    inner class WeatherListAdapterHeaderViewHolder(val binding: AdapterWeatherListHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindViewHolder() {
            binding.executePendingBindings()
        }
    }
//
//    override fun onCreateViewHolder(
//        parent: ViewGroup,
//        viewType: Int
//    ): WeatherListAdapterViewHolder = WeatherListAdapterViewHolder(
//        DataBindingUtil.inflate(
//            LayoutInflater.from(parent.context),
//            R.layout.adapter_weather_list_item,
//            parent,
//            false
//        )
//    )

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {

        if (viewType == VIEWTYPE_HEADER) {
            return WeatherListAdapterHeaderViewHolder(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.adapter_weather_list_header,
                    parent,
                    false
                )
            )
        } else {
            return WeatherListAdapterViewHolder(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.adapter_weather_list_item,
                    parent,
                    false
                )
            )
        }


    }

    override fun getItemCount(): Int {
        if (this.list?.size == 0) {
            return 0
        } else {
            return (this.list?.size ?: 0) + 1
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (getItemViewType(position) == VIEWTYPE_CONTENT) {
            this.list?.get(position - 1)?.let {
                (holder as WeatherListAdapterViewHolder).bindViewHolder(it)
            }
        }
    }
}