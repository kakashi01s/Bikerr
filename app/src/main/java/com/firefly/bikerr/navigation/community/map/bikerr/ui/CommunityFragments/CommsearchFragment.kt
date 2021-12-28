package com.firefly.bikerr.navigation.community.map.bikerr.ui.CommunityFragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firefly.bikerr.navigation.community.map.bikerr.R
import com.firefly.bikerr.navigation.community.map.bikerr.adapters.ChannelAdapter
import com.firefly.bikerr.navigation.community.map.bikerr.adapters.NearbyAdapter
import com.firefly.bikerr.navigation.community.map.bikerr.model.Nearby.Geometry
import com.firefly.bikerr.navigation.community.map.bikerr.ui.ChatActivities.MychatsActivity
import com.google.android.libraries.places.internal.i
import com.google.firebase.auth.FirebaseAuth
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.api.models.QueryChannelsRequest
import io.getstream.chat.android.client.api.models.QuerySort
import io.getstream.chat.android.client.models.Channel
import io.getstream.chat.android.client.models.Filters

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CommsearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CommsearchFragment : Fragment(), ChannelAdapter.ChannelitemClickListener {
    // TODO: Rename and change types of parameters
    private var param1: Int? = null
    private var param2: String? = null

    var channelAdapter: ChannelAdapter? = null
    var rvchannels : RecyclerView? = null
    var joinBikerr : Button? = null

    val client = ChatClient.instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getInt(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_commsearch, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        getChannels()
        joinBikerr?.setOnClickListener {

            val uid = FirebaseAuth.getInstance().currentUser?.uid
            val channelClient = client.channel("messaging", "Bikerr")

            // Add members with ids
            channelClient.addMembers(uid!!).enqueue { result ->
                if (result.isSuccess) {
                    val channel: Channel = result.data()
                    val intent = Intent(activity, MychatsActivity::class.java)
                    startActivity(intent)
                } else {
                    // Handle result.error()
                    Log.d("joinchannel", result.error().message.toString())
                    Toast.makeText(requireContext(), "Error Joining Community", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }

    private fun initViews(view: View) {
        rvchannels = view.findViewById(R.id.rv_channels)
        joinBikerr = view.findViewById(R.id.join_bikerr_official)
    }

    fun getChannels(){
        val request = QueryChannelsRequest(
            filter = Filters.and(
                Filters.eq("type", "messaging"),
            ),
            offset = 0,
            limit = 10,
            querySort = QuerySort.desc("last_message_at")
        ).apply {
            watch = true
            state = true
        }

        client.queryChannels(request).enqueue { result ->
            if (result.isSuccess) {
                val channels: List<Channel> = result.data()
                Log.d("searchch" , channels.toString())
                for ( i in channels)
                {
                    channelAdapter = ChannelAdapter(requireContext(), channels, this)
                    rvchannels?.adapter = channelAdapter
                    rvchannels?.layoutManager = LinearLayoutManager(requireContext())

                }
            } else {
                // Handle result.error()
                Log.d("searchch", result.error().message.toString())
                Toast.makeText(requireContext(), "Error Fetching Channels", Toast.LENGTH_SHORT).show()
            }
        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CommsearchFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: Int, param2: String) =
            CommsearchFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun OnChannelCardClick(position: Int, channelid: String) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        val channelClient = client.channel("messaging", channelid)

        // Add members with ids
        channelClient.addMembers(uid!!).enqueue { result ->
            if (result.isSuccess) {
                val channel: Channel = result.data()
                val intent = Intent(activity, MychatsActivity::class.java)
                startActivity(intent)
            } else {
                // Handle result.error()
                Log.d("joinchannel", result.error().message.toString())
                Toast.makeText(requireContext(), "Error Joining Community", Toast.LENGTH_SHORT).show()
            }
        }
    }


}