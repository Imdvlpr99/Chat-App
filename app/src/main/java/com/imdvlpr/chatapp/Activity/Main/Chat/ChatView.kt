package com.imdvlpr.chatapp.Activity.Main.Chat

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import com.imdvlpr.chatapp.Activity.Main.Search.DateTimeAdapter
import com.imdvlpr.chatapp.Model.ChatMessage
import com.imdvlpr.chatapp.Model.Message
import com.imdvlpr.chatapp.Model.StatusResponse
import com.imdvlpr.chatapp.Model.Users
import com.imdvlpr.chatapp.Shared.Extension.*
import com.imdvlpr.chatapp.databinding.ActivityChatBinding
import java.util.*
import kotlin.collections.ArrayList

class ChatView : AppCompatActivity() {

    companion object {
        private const val USER_DATA = "userData"
        fun newIntent(context: Context, users: Users): Intent {
            val intent = Intent(context, ChatView::class.java)
            intent.putExtra(USER_DATA, users)
            return intent
        }
    }

    private lateinit var binding: ActivityChatBinding
    private lateinit var adapter: ChatAdapter
    private lateinit var preferenceManager: PreferenceManager
    private lateinit var userData: Users
    private lateinit var message: ArrayList<Message>
    private lateinit var database: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBundle()
        initView()
        listenMessage()
    }

    private fun initBundle() {
        userData = intent.getParcelable(USER_DATA, Users::class.java) ?: Users()
    }

    private fun initView() {
        binding.userName.text = userData.name
        binding.userImage.setImageBitmap(decodeImage(userData.image))

        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
            finish()
        }

        preferenceManager = PreferenceManager(applicationContext)
        message = ArrayList()
        database = FirebaseFirestore.getInstance()

        binding.btnSend.setOnClickListener {
            if (binding.etEntry.text.toString().isNotEmpty()) {
                sendMessage()
            }
        }
    }

    private fun sendMessage() {
        val message = hashMapOf(
            Constants.KEY_CHATS.KEY_SENDER_ID to preferenceManager.getString(Constants.KEY_USERS.KEY_USER_ID).toString(),
            Constants.KEY_CHATS.KEY_RECEIVER_ID to userData.id,
            Constants.KEY_CHATS.KEY_MESSAGE to binding.etEntry.text.toString(),
            Constants.KEY_CHATS.KEY_IS_READ to false,
            Constants.KEY_CHATS.KEY_TIMESTAMP to Date())
        database.collection(Constants.KEY_CHATS.KEY_COLLECTION_CHAT).add(message)
        binding.etEntry.text = null
        updateReadReceipt()
    }

    private fun listenMessage() {
        database.collection(Constants.KEY_CHATS.KEY_COLLECTION_CHAT)
            .whereEqualTo(Constants.KEY_CHATS.KEY_SENDER_ID, preferenceManager.getString(Constants.KEY_USERS.KEY_USER_ID))
            .whereEqualTo(Constants.KEY_CHATS.KEY_RECEIVER_ID, userData.id)
            .addSnapshotListener(eventListener)
        database.collection(Constants.KEY_CHATS.KEY_COLLECTION_CHAT)
            .whereEqualTo(Constants.KEY_CHATS.KEY_SENDER_ID, userData.id)
            .whereEqualTo(Constants.KEY_CHATS.KEY_RECEIVER_ID, preferenceManager.getString(Constants.KEY_USERS.KEY_USER_ID))
            .addSnapshotListener(eventListener)
    }

    private fun updateReadReceipt() {
        database.collection(Constants.KEY_CHATS.KEY_COLLECTION_CHAT)
            .whereEqualTo(Constants.KEY_CHATS.KEY_RECEIVER_ID, preferenceManager.getString(Constants.KEY_USERS.KEY_USER_ID))
            .whereEqualTo(Constants.KEY_CHATS.KEY_SENDER_ID, userData.id)
            .get().addOnCompleteListener { task ->
                val querySnapshot = task.result ?: return@addOnCompleteListener
                for (document in querySnapshot.documents){
                    document.reference.update(Constants.KEY_CHATS.KEY_IS_READ, true)
                        .addOnSuccessListener {
                            //listenMessage()
                        }
                        .addOnFailureListener {
                            Log.d("success", false.toString())
                        }
                }
            }
    }

    @SuppressLint("NotifyDataSetChanged")
    private val eventListener: EventListener<QuerySnapshot> = EventListener { value, error ->
        updateReadReceipt()
        if (error != null) {
            return@EventListener
        }
        if (value != null) {
            val count = message.size
            for (documentChange: DocumentChange in value.documentChanges) {
                if (documentChange.type == DocumentChange.Type.ADDED) {
                    val messages = Message()
                    messages.senderId = documentChange.document.getString(Constants.KEY_CHATS.KEY_SENDER_ID).toString()
                    messages.receiverId = documentChange.document.getString(Constants.KEY_CHATS.KEY_RECEIVER_ID).toString()
                    messages.message = documentChange.document.getString(Constants.KEY_CHATS.KEY_MESSAGE).toString()
                    messages.dateTime = getReadableDate(documentChange.document.getDate(Constants.KEY_CHATS.KEY_TIMESTAMP)!!)
                    messages.isRead = documentChange.document.getBoolean(Constants.KEY_CHATS.KEY_IS_READ)!!
                    messages.dateObject = documentChange.document.getDate(Constants.KEY_CHATS.KEY_TIMESTAMP)!!
                    message.add(messages)
                }
            }

            val sorted = message.sortedWith(compareBy { it.dateObject })
            adapter = ChatAdapter(sorted, preferenceManager.getString(Constants.KEY_USERS.KEY_USER_ID).toString())
            binding.chatRecycler.adapter = adapter
            if (count == 0) {
                adapter.notifyDataSetChanged()
            } else {
                adapter.notifyItemRangeInserted(sorted.size, sorted.size)
                binding.chatRecycler.smoothScrollToPosition(sorted.size - 1)
            }
        }
    }

}
