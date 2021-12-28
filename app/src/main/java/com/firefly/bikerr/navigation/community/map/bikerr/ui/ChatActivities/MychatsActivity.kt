package com.firefly.bikerr.navigation.community.map.bikerr.ui.ChatActivities

import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.viewModels
import com.firefly.bikerr.navigation.community.map.bikerr.databinding.ActivityMychatsBinding
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.models.Filters
import io.getstream.chat.android.client.models.User
import io.getstream.chat.android.ui.channel.list.ChannelListView
import io.getstream.chat.android.ui.channel.list.viewmodel.ChannelListViewModel
import io.getstream.chat.android.ui.channel.list.viewmodel.bindView
import io.getstream.chat.android.ui.channel.list.viewmodel.factory.ChannelListViewModelFactory

class MychatsActivity : AppCompatActivity() {
    val client = ChatClient.instance()
    var binding_ : ActivityMychatsBinding? = null
    val streamuser = client.getCurrentUser()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding_ = ActivityMychatsBinding.inflate(layoutInflater)
        setContentView(binding_!!.root)
        //streamchat user connection
        binding_!!.chaterror.visibility = View.INVISIBLE
        if (streamuser != null)
        {
            getStreamchatchannels(streamuser)
        }
        else{
            binding_!!.chaterror.visibility = View.VISIBLE
        }

    }

    private fun getStreamchatchannels(user: User) {
        // Step 3 - Set the channel list filter and order
        // This can be read as requiring only channels whose "type" is "messaging" AND
        // whose "members" include our "user.id"
        val filter = Filters.and(
            Filters.eq("type", "messaging"),
            Filters.`in`("members", listOf(user.id))
        )
        val viewModelFactory = ChannelListViewModelFactory(filter, ChannelListViewModel.DEFAULT_SORT)
        val viewModel: ChannelListViewModel by viewModels { viewModelFactory }

        // Step 4 - Connect the ChannelListViewModel to the ChannelListView, loose
        //          coupling makes it easy to customize
        viewModel.bindView(binding_!!.channelListView, this)
        binding_!!.channelListView.setChannelItemClickListener { channel ->
            startActivity(MymessagingActivity.newIntent(this, channel))
        }

    }


}






