package com.imdvlpr.chatapp.Activity.Main.Search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.imdvlpr.chatapp.Activity.Main.Chat.ChatAdapter
import com.imdvlpr.chatapp.Model.ChatMessage
import com.imdvlpr.chatapp.R
import com.imdvlpr.chatapp.Shared.Extension.getReadableDate
import com.imdvlpr.chatapp.databinding.ItemChatDateBinding
import com.imdvlpr.chatapp.databinding.ItemUsersBinding
import java.util.*
import kotlin.collections.ArrayList

class DateTimeAdapter(
    val senderId: String,
    private var data: ArrayList<ChatMessage>) : RecyclerView.Adapter<DateTimeAdapter.DateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateViewHolder {
        return DateViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_chat_date, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: DateViewHolder, position: Int) =
        holder.bind(data[position])

    inner class DateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: ChatMessage) = with(itemView) {
            val binding = ItemChatDateBinding.bind(itemView)

            binding.dateTime.text = item.dateTime
            binding.charRecycler.adapter = ChatAdapter(item.data, senderId)
        }
    }
}