package com.imdvlpr.chatapp.Activity.Main.Search

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.imdvlpr.chatapp.Model.Users
import com.imdvlpr.chatapp.R
import com.imdvlpr.chatapp.Shared.Extension.decodeImage
import com.imdvlpr.chatapp.databinding.ItemUsersBinding
import java.util.*
import kotlin.collections.ArrayList

class UsersAdapter(
    private var data: ArrayList<Users>,
    private val listener: UsersListener
) : RecyclerView.Adapter<UsersAdapter.UsersViewHolder>() {

    var dataToShow = ArrayList<Users>()
    private var charTextSearch = ""

    init {
        dataToShow.addAll(processDataToShow())
    }

    fun processDataToShow(): List<Users> {
        return data
    }

    @SuppressLint("NotifyDataSetChanged")
    fun search(charText: String) {
        charTextSearch = charText.lowercase(Locale.getDefault())
        dataToShow.clear()
        if (charTextSearch.isEmpty()) {
            dataToShow.addAll(processDataToShow())
            notifyDataSetChanged()
        } else {
            for (users in data) {
                if (users.email.lowercase(Locale.getDefault()).contains(charTextSearch) ||
                        users.name.lowercase(Locale.getDefault()).contains(charTextSearch)) {
                    dataToShow.add(users)
                }
            }
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        return UsersViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_users, parent, false)
        )
    }

    override fun getItemCount() = dataToShow.size

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) =
        holder.bind(dataToShow[position], listener)

    inner class UsersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Users, listener: UsersListener) = with(itemView) {
            val binding = ItemUsersBinding.bind(itemView)

            binding.userEmail.text = item.email
            binding.userName.text = item.name
            binding.userImage.setImageBitmap(decodeImage(item.image))

            setOnClickListener {
                listener.onUserClick(item)
            }
        }
    }

    interface UsersListener {
        fun onUserClick(item: Users)
    }
}