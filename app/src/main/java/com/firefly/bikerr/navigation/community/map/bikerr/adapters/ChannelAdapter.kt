package com.firefly.bikerr.navigation.community.map.bikerr.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firefly.bikerr.navigation.community.map.bikerr.R
import com.firefly.bikerr.navigation.community.map.bikerr.adapters.ChannelAdapter.ChannelsVIewHolder
import com.firefly.bikerr.navigation.community.map.bikerr.model.Nearby.Geometry

class ChannelAdapter(
    var context: Context,
    var channels: List<io.getstream.chat.android.client.models.Channel>,
    var listener: ChannelitemClickListener
) : RecyclerView.Adapter<ChannelsVIewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChannelsVIewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.card_channels, parent, false)
        return ChannelsVIewHolder(view)
    }

    override fun onBindViewHolder(holder: ChannelsVIewHolder, position: Int) {
        val channels = channels[position]
        val channelname = channels.id
        val channelmembers = channels.memberCount

        holder.channelName.text = channelname
        holder.channelMems.text = channelmembers.toString()
    }

    override fun getItemCount(): Int {
        return  channels.size
    }

    inner class ChannelsVIewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var channelName = itemView.findViewById<TextView>(R.id.channel_name)
        var channelMems = itemView.findViewById<TextView>(R.id.channel_members)
        var joinchannel = itemView.findViewById<Button>(R.id.join_button)

        init {
            joinchannel.setOnClickListener {
                val position = adapterPosition
                val channelid = channels[position].id
                listener.OnChannelCardClick(position,channelid)
            }

        }

    }

    interface ChannelitemClickListener {
        fun OnChannelCardClick(position: Int, channelid: String)

    }


}