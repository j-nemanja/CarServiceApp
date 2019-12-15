package com.example.carservice.car.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.carservice.Failure
import com.example.carservice.MainViewModel
import com.example.carservice.item.Placemark
import com.example.carservice.R
import kotlinx.android.synthetic.main.fragment_car_info_list.*

class CarInfoListFragment : Fragment() {

    private fun layoutId() = R.layout.fragment_car_info_list

    private lateinit var mainViewModel: MainViewModel
    private lateinit var adapter: CarInfoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = CarInfoAdapter()
        activity?.let { activity ->
            mainViewModel = ViewModelProviders.of(activity).get(MainViewModel::class.java)
            mainViewModel.carList.observe(this, Observer { renderCarInfoList(it) })
            mainViewModel.placemarksRequestFailure.observe(
                this,
                Observer { renderPlacemarksRequestFailure(it) }
            )
        }
    }

    private fun renderPlacemarksRequestFailure(failure: Failure?) {
        failure?.let {
            error_message_holder.text = failure.message
        }
    }

    private fun renderNoData() {
        error_message_holder.text = getString(R.string.no_items_available)
    }

    private fun renderCarInfoList(placemarkList: List<Placemark>?) {
        if (placemarkList.isNullOrEmpty()) {
            renderNoData()
        } else {
            adapter.items = placemarkList
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutId(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        car_info_list.adapter = adapter
    }

}