package com.firefly.bikerr.navigation.community.map.bikerr.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.firefly.bikerr.navigation.community.map.bikerr.databinding.FragmentChannelsBinding
import com.firefly.bikerr.navigation.community.map.bikerr.ui.ChatActivities.MymessagingActivity
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.models.Filters
import io.getstream.chat.android.client.models.User
import io.getstream.chat.android.ui.channel.list.viewmodel.ChannelListViewModel
import io.getstream.chat.android.ui.channel.list.viewmodel.bindView
import io.getstream.chat.android.ui.channel.list.viewmodel.factory.ChannelListViewModelFactory
import kotlinx.coroutines.delay

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ChannelsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChannelsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: Int? = null
    private var param2: String? = null
    var binding : FragmentChannelsBinding? = null
    private val client = ChatClient.instance()
    val streamuser = client.getCurrentUser()

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
        binding = FragmentChannelsBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding!!.chaterror.visibility = View.INVISIBLE
        if (streamuser != null)
        {
            getChannels(streamuser)
        }
        else{
            binding!!.chaterror.visibility = View.VISIBLE
        }

    }

    private fun getChannels(streamuser: User) {
        // Instantiate the ViewModel
        val filters = Filters.and(
            Filters.eq("type", "messaging"),
            Filters.`in`("members", listOf(streamuser.id))
        )
        val viewModelFactory = ChannelListViewModelFactory(
            filters,
            ChannelListViewModel.DEFAULT_SORT
        )
        val listViewModel: ChannelListViewModel by viewModels { viewModelFactory }

        listViewModel.bindView(binding!!.channelListView, viewLifecycleOwner)
        binding!!.channelListView.setChannelItemClickListener(){ channel ->
            startActivity(MymessagingActivity.newIntent(context!!,channel))
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ChannelsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: Int, param2: String) =
            ChannelsFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}