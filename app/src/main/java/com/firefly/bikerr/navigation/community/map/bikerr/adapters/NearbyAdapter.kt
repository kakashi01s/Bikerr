package com.firefly.bikerr.navigation.community.map.bikerr.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firefly.bikerr.navigation.community.map.bikerr.R
import com.firefly.bikerr.navigation.community.map.bikerr.model.Nearby.Geometry
import com.firefly.bikerr.navigation.community.map.bikerr.model.Nearby.Result


class NearbyAdapter(var context: Context, var places: List<Result> ,val listener: NearbyitemClickListener):
    RecyclerView.Adapter<NearbyAdapter.NearbyVIewHolder>(){







    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NearbyVIewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.card_nearby, parent, false)
        return NearbyVIewHolder(view)
    }

    override fun onBindViewHolder(holder: NearbyVIewHolder, position: Int) {
        val nearby = places[position]

        val nearbyRating = nearby.rating



        holder.NearbyName.text = nearby.name
        holder.NearbyRating.text = nearbyRating.toString()
        holder.nearbyadd.text = nearby.vicinity





    }

    override fun getItemCount(): Int {
        return places.size
    }


    inner class NearbyVIewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var NearbyName = itemView.findViewById<TextView>(R.id.nearby_Name)
        var NearbyRating = itemView.findViewById<TextView>(R.id.nearby_Rating)
        var getdir = itemView.findViewById<Button>(R.id.getdir_nearby)
        var nearbyadd = itemView.findViewById<TextView>(R.id.nearby_address)
        init {
            getdir.setOnClickListener {
                val position = adapterPosition
                val geometry = places[position].geometry
                listener.OnNearbyCardClick(position,geometry)
            }

        }
    }

    interface NearbyitemClickListener{
        fun OnNearbyCardClick(position : Int ,geometry: Geometry)
    }



}