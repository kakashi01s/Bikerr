package com.firefly.bikerr.navigation.community.map.bikerr.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firefly.bikerr.navigation.community.map.bikerr.R
import com.google.firebase.firestore.DocumentSnapshot

class SparePartsAdapter(
    var context: Context,
    var items: List<DocumentSnapshot>,
    var listener: SparePartsitemClickListener
) : RecyclerView.Adapter<SparePartsAdapter.SparePartsVIewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SparePartsVIewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.card_allappsportall_layout, parent, false)
        return SparePartsVIewHolder(view)
    }

    override fun onBindViewHolder(holder: SparePartsVIewHolder, position: Int) {
        val item = items[position]
       holder.itemName.text = item.get("Name").toString()

        Glide.with(context)
            .load(item.get("Image").toString())
            .into(holder.itemimg)

    }

    override fun getItemCount(): Int {
        return items.size
    }



    inner class SparePartsVIewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemName = itemView.findViewById<TextView>(R.id.tvPortalName)

        var itemimg = itemView.findViewById<ImageView>(R.id.ivAllAppsPortal)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                val itemname = items[position].get("Name").toString()
                val itemurl = items[position].get("url").toString()
                listener.OnSparePartsCardClick(position,itemname,itemurl)
            }

        }

    }

    interface SparePartsitemClickListener {
        fun OnSparePartsCardClick(position: Int, itemname: String, itemurl: String)

    }



}