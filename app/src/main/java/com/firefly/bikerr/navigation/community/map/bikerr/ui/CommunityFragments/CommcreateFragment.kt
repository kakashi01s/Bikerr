package com.firefly.bikerr.navigation.community.map.bikerr.ui.CommunityFragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.firefly.bikerr.navigation.community.map.bikerr.databinding.FragmentCommcreateBinding
import com.firefly.bikerr.navigation.community.map.bikerr.ui.ChatActivities.MychatsActivity
import com.google.firebase.auth.FirebaseAuth
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.models.Channel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CommcreateFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CommcreateFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: Int? = null
    private var param2: String? = null

    private var binding_ : FragmentCommcreateBinding? = null
    private val client = ChatClient.instance()

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
        binding_ = FragmentCommcreateBinding.inflate(inflater, container,false)


        return binding_!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        binding_?.createChannel?.setOnClickListener {
            val name: String = binding_!!.channelName.text.toString().trim()
            val city = binding_!!.channelCity.text.toString().trim()
            var id = "$name"+"$city"
            val hashMap : HashMap<String, Any>
                    = HashMap()
            //adding elements to the hashMap using
            // put() function
            hashMap.put("name" , name)

            val channelClient = client.channel(channelType = "messaging", channelId = id)
            channelClient.create(members = listOf(uid) ,extraData =  hashMap).enqueue { result ->
                if (result.isSuccess) {
                    val newChannel: Channel = result.data()

                    Log.d("newchannel", newChannel.id)
                    val intent = Intent(activity , MychatsActivity::class.java)
                    startActivity(intent)
                    Toast.makeText(requireContext(), "Channel Created", Toast.LENGTH_SHORT).show()
                } else {
                    Log.d("newchannel", result.error().message.toString())
                    Toast.makeText(requireContext(), "Error Creating Channel", Toast.LENGTH_SHORT).show()
                }
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
         * @return A new instance of fragment CommcreateFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: Int, param2: String) =
            CommcreateFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}