package com.firefly.bikerr.navigation.community.map.bikerr.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firefly.bikerr.navigation.community.map.bikerr.R
import com.firefly.bikerr.navigation.community.map.bikerr.model.Cart.CartItem
import com.firefly.bikerr.navigation.community.map.bikerr.model.Cart.OrderItem

class OrderAdapter(
    var context: Context,
    var items: List<OrderItem>?
) : RecyclerView.Adapter<OrderAdapter.OrderVIewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderAdapter.OrderVIewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.card_cart, parent, false)
        return OrderVIewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderAdapter.OrderVIewHolder, position: Int) {
        val item = items!![position]
        holder.itemName.text = item.name
        holder.itemprice.text = item.price.toString()

    }

    override fun getItemCount(): Int {
        return items!!.size
    }


    inner class OrderVIewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemName = itemView.findViewById<TextView>(R.id.order_item_name)
        var itemprice = itemView.findViewById<TextView>(R.id.order_item_price)



    }
}

