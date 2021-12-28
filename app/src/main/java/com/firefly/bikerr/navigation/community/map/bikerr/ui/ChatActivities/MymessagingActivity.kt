package com.firefly.bikerr.navigation.community.map.bikerr.ui.ChatActivities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.addCallback
import androidx.activity.viewModels
import com.firefly.bikerr.navigation.community.map.bikerr.databinding.ActivityMymessagingBinding
import com.getstream.sdk.chat.viewmodel.MessageInputViewModel
import com.getstream.sdk.chat.viewmodel.messages.MessageListViewModel
import io.getstream.chat.android.client.models.Channel
import io.getstream.chat.android.ui.message.input.viewmodel.bindView
import io.getstream.chat.android.ui.message.list.header.viewmodel.MessageListHeaderViewModel
import io.getstream.chat.android.ui.message.list.header.viewmodel.bindView
import io.getstream.chat.android.ui.message.list.viewmodel.bindView
import io.getstream.chat.android.ui.message.list.viewmodel.factory.MessageListViewModelFactory

class MymessagingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMymessagingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Step 0 - inflate binding
        binding = ActivityMymessagingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val cid = checkNotNull(intent.getStringExtra(CID_KEY)) {
            "Specifying a channel id is required when starting ChannelActivity"
        }

        // Step 1 - Create three separate ViewModels for the views so it's easy
        //          to customize them individually
        val factory = MessageListViewModelFactory(cid)
        val messageListHeaderViewModel: MessageListHeaderViewModel by viewModels { factory }
        val messageListViewModel: MessageListViewModel by viewModels { factory }
        val messageInputViewModel: MessageInputViewModel by viewModels { factory }

        // TODO set custom Imgur attachment factory

        // Step 2 - Bind the view and ViewModels, they are loosely coupled so it's easy to customize
        messageListHeaderViewModel.bindView(binding.messageListHeaderView, this)
        messageListViewModel.bindView(binding.messageListView, this)
        messageInputViewModel.bindView(binding.messageInputView, this)

        // Step 3 - Let both MessageListHeaderView and MessageInputView know when we open a thread
        messageListViewModel.mode.observe(this) { mode ->
            when (mode) {
                is MessageListViewModel.Mode.Thread -> messageInputViewModel.setActiveThread(mode.parentMessage)
                is MessageListViewModel.Mode.Normal -> messageInputViewModel.resetThread()
            }
        }

    // Step 4 - Let the message input know when we are editing a message
    binding.messageListView.setMessageEditHandler(messageInputViewModel::postMessageToEdit)

    // Step 5 - Handle navigate up state
    messageListViewModel.state.observe(this)
    {
        state ->
        if (state is MessageListViewModel.State.NavigateUp) {
            finish()
        }
    }

    // Step 6 - Handle back button behaviour correctly when you're in a thread
    val backHandler = {
        messageListViewModel.onEvent(MessageListViewModel.Event.BackButtonPressed)
    }
    binding.messageListHeaderView.setBackButtonClickListener(backHandler)
    onBackPressedDispatcher.addCallback(this)
    {
        backHandler()
    }
}


    companion object {
        private const val CID_KEY = "key:cid"

        fun newIntent(context: Context, channel: Channel): Intent =
            Intent(context, MymessagingActivity::class.java).putExtra(CID_KEY, channel.cid)
    }
}

