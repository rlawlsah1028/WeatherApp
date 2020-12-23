package com.bp.weatherapp.views.activities

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.bp.weatherapp.R
import com.bp.weatherapp.databinding.ActivityMainBinding
import com.bp.weatherapp.repositories.WeatherRepository
import com.bp.weatherapp.viewModels.WeatherViewModel
import com.bp.weatherapp.views.adapter.WeatherListAdapter
import com.bumptech.glide.Glide
import com.bp.weatherapp.viewModels.VMFactory
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable

class MainActivity : BaseActivity() {


    var compositeDisposable = CompositeDisposable()

    val adapter by lazy {
        WeatherListAdapter(Glide.with(this))?.let {
            it.setHasStableIds(true)
            it
        }
    }


    val weatherViewModel: WeatherViewModel by lazy {
        ViewModelProvider(
            this,
            VMFactory(WeatherRepository.getInstance())
        ).get(WeatherViewModel::class.java)
    }
//

    private val binding by binding<ActivityMainBinding>(R.layout.activity_main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding.recyclerView.adapter = this.adapter

        Log.d("test", "시작")
        this.showLoading()
        this.compositeDisposable.add(
            this.weatherViewModel.getAllData("se")
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe({
                    Log.d("test", "결과 : " + it.size)
                    this.adapter.list = it
                }, {
                    it.printStackTrace()
                }, {
                    this.hideLoading()
                })
        )

        this.binding.swipeRefreshLayout.setOnRefreshListener {
            this.adapter.list = arrayOf()
            this.compositeDisposable.add(
                this.weatherViewModel.getAllData("se")
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe({
                        this.adapter.list = it
                    }, {
                        it.printStackTrace()
                    }, {
                        this.binding.swipeRefreshLayout.isRefreshing = false
                    })
            )
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        this.compositeDisposable.clear()
    }

}