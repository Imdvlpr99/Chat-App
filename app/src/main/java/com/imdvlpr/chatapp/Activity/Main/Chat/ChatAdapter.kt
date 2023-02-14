package com.imdvlpr.chatapp.Activity.Main.Chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.imdvlpr.chatapp.Model.Message
import com.imdvlpr.chatapp.R
import com.imdvlpr.chatapp.databinding.ItemReceiveMessageBinding
import com.imdvlpr.chatapp.databinding.ItemSentMessageBinding

class ChatAdapter(
    private var data: List<Message>, var id: String) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val VIEW_TYPE_SENT = 1
    private val VIEW_TYPE_RECEIVER = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_SENT) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_sent_message, parent, false)
            SenderViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_receive_message, parent, false)
            ReceiverViewHolder(view)
        }
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == VIEW_TYPE_SENT) {
            (holder as SenderViewHolder).bind(data[position])
        } else {
            (holder as ReceiverViewHolder).bind(data[position])
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (data[position].senderId == id) {
            VIEW_TYPE_SENT
        } else {
            VIEW_TYPE_RECEIVER
        }
    }

    inner class SenderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Message) = with(itemView) {
            val binding = ItemSentMessageBinding.bind(itemView)
            binding.txtMessage.text = item.message
            binding.txtDate.text = item.dateTime

            when(item.isRead) {
                true -> binding.icRead.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary))
                false -> binding.icRead.setColorFilter(ContextCompat.getColor(context, R.color.colorGrayMedium))
            }
        }
    }


    inner class ReceiverViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Message) = with(itemView) {
            val binding = ItemReceiveMessageBinding.bind(itemView)

            binding.txtMessage.text = item.message
            binding.txtDate.text = item.dateTime
        }
    }
}