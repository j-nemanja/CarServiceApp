package com.example.carservice.car.info

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.carservice.item.Placemark
import com.example.carservice.R
import kotlinx.android.synthetic.main.item_car.view.*
import kotlin.properties.Delegates

class CarInfoAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    internal var items: List<Placemark> by Delegates.observable(emptyList()) { _, _, _ ->
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_car, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        holder.itemView.name.text = getFormattedValue(holder,
            R.string.name, item.name)
        holder.itemView.engine_type.text = getFormattedValue(holder,
            R.string.engine_type,
            item.engineType)
        holder.itemView.interior.text = getFormattedValue(holder,
            R.string.interior, item.interior)
        holder.itemView.exterior.text = getFormattedValue(holder,
            R.string.exterior, item.exterior)
        holder.itemView.fuel.text = getFormattedValue(holder,
            R.string.fuel, item.fuel.toString())
        holder.itemView.vin.text = getFormattedValue(holder,
            R.string.vin, item.vin)
}

    private fun getFormattedValue(
        holder : RecyclerView.ViewHolder,
        resId : Int,
        value : String
    ): String {
       return holder.itemView.context.getString(resId) + " " + value
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
}