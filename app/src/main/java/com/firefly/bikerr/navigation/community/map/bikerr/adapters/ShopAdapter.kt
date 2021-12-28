package com.firefly.bikerr.navigation.community.map.bikerr.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firefly.bikerr.navigation.community.map.bikerr.R
import com.github.chrisbanes.photoview.PhotoView
import com.google.firebase.firestore.DocumentSnapshot

class ShopAdapter(
    var context: Context,
    var items: List<DocumentSnapshot>,
    var listener: ShopitemClickListener
) : RecyclerView.Adapter<ShopAdapter.ShopVIewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopAdapter.ShopVIewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.card_shop, parent, false)
        return ShopVIewHolder(view)
    }

    override fun onBindViewHolder(holder: ShopVIewHolder , position: Int) {
        val item = items[position]
        holder.itemName.text = item.get("Name").toString()
        holder.itemprice.text = item.get("Price").toString()

        Glide.with(context)
            .load(item.get("Image").toString())
            .into(holder.itemimg)

    }

    override fun getItemCount(): Int {
        return items.size
    }



    inner class ShopVIewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemName = itemView.findViewById<TextView>(R.id.item_name)
        var itemprice = itemView.findViewById<TextView>(R.id.item_price)
        var itemimg = itemView.findViewById<PhotoView>(R.id.item_img)
        var viewitem = itemView.findViewById<Button>(R.id.button_viewItem)

        init {
            viewitem.setOnClickListener {
                val itemname = items[adapterPosition].get("Name").toString()
                val itemprice = items[adapterPosition].get("Price")
                val itemimg = items[adapterPosition].get("Image").toString()
                listener.OnShopCardClick(adapterPosition,itemname,itemprice,itemimg)
            }

        }

    }

    interface ShopitemClickListener {
        fun OnShopCardClick(position: Int, itemname: String, itemprice: Any?, itemimg: String)

    }



}