package com.imdvlpr.chatapp.Shared.Network

import android.content.Context
import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import com.imdvlpr.chatapp.Model.StatusResponse
import com.imdvlpr.chatapp.Model.Users
import com.imdvlpr.chatapp.Shared.Extension.Constants
import com.imdvlpr.chatapp.Shared.Extension.PreferenceManager

class FirestoreConnection(val context: Context) {

    private var preferenceManager: PreferenceManager = PreferenceManager(context)
    private val database = FirebaseFirestore.getInstance()

    fun registerUser(name: String, email: String, password: String, userImage: String, callback: (StatusResponse) -> Unit) {
        database.collection(Constants.KEY_USERS.KEY_COLLECTION_USERS)
            .whereEqualTo(Constants.KEY_USERS.KEY_EMAIL, email)
            .get()
            .addOnSuccessListener {
                callback(StatusResponse(isSuccess = false, message = "Email sudah dipakai"))
            }
            .addOnFailureListener {
                val user = hashMapOf(
                    Constants.KEY_USERS.KEY_NAME to name,
                    Constants.KEY_USERS.KEY_EMAIL to email,
                    Constants.KEY_USERS.KEY_PASSWORD to password,
                    Constants.KEY_USERS.KEY_IMAGE to userImage
                )

                database.collection(Constants.KEY_USERS.KEY_COLLECTION_USERS)
                    .add(user)
                    .addOnSuccessListener {
                        callback(StatusResponse(isSuccess = true))
                    }
                    .addOnFailureListener {
                        callback(StatusResponse(isSuccess = false, message = "Server Error"))
                        Log.e("error", it.message.toString())
                    }
            }

    }

    fun login(email: String, password: String, callback: (StatusResponse) -> Unit) {
        database.collection(Constants.KEY_USERS.KEY_COLLECTION_USERS)
            .whereEqualTo(Constants.KEY_USERS.KEY_EMAIL, email)
            .whereEqualTo(Constants.KEY_USERS.KEY_PASSWORD, password)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful && task.result != null && task.result.documents.size > 0) {
                    val documentSnapshot: DocumentSnapshot = task.result.documents[0]
                    preferenceManager.putBoolean(Constants.KEY_USERS.KEY_IS_SIGNED_IN, true)
                    preferenceManager.putString(Constants.KEY_USERS.KEY_USER_ID, documentSnapshot.id)
                    preferenceManager.putString(Constants.KEY_USERS.KEY_NAME, documentSnapshot.getString(Constants.KEY_USERS.KEY_NAME).toString())
                    preferenceManager.putString(Constants.KEY_USERS.KEY_EMAIL, documentSnapshot.getString(Constants.KEY_USERS.KEY_EMAIL).toString())
                    preferenceManager.putString(Constants.KEY_USERS.KEY_IMAGE, documentSnapshot.getString(Constants.KEY_USERS.KEY_IMAGE).toString())
                    callback(StatusResponse(isSuccess = true))
                } else {
                    callback(StatusResponse(isSuccess = false, errorCode = "203", message = "Password Salah !"))
                }
            }
            .addOnFailureListener {
                callback(StatusResponse(isSuccess = false, errorCode = "503",message = "Server Error"))
                Log.e("error", it.message.toString())
            }
    }

    fun updateToken(fcmToken: String, callback: (StatusResponse) -> Unit) {
        val documentReference = database.collection(Constants.KEY_USERS.KEY_COLLECTION_USERS).document(
            preferenceManager.getString(Constants.KEY_USERS.KEY_USER_ID).toString()
        )
        documentReference.update(Constants.KEY_USERS.KEY_FCM_TOKEN, fcmToken)
            .addOnCompleteListener {
                callback(StatusResponse(isSuccess = true))
            }
            .addOnFailureListener {
                callback(StatusResponse(isSuccess = false))
            }
    }

    fun logout(callback: (StatusResponse) -> Unit) {
        val documentReference = database.collection(Constants.KEY_USERS.KEY_COLLECTION_USERS).document(
            preferenceManager.getString(Constants.KEY_USERS.KEY_USER_ID).toString()
        )
        val update = hashMapOf(
            Constants.KEY_USERS.KEY_FCM_TOKEN to FieldValue.delete())
        documentReference.update(update as Map<String, Any>)
            .addOnSuccessListener {
                preferenceManager.clear()
                callback(StatusResponse(isSuccess = true))
            }
            .addOnFailureListener {
                callback(StatusResponse(isSuccess = false))
            }
    }

    fun getUserList(callback: (ArrayList<Users>, StatusResponse) -> Unit) {
        database.collection(Constants.KEY_USERS.KEY_COLLECTION_USERS)
            .get()
            .addOnCompleteListener {
                val currentUserId = preferenceManager.getString(Constants.KEY_USERS.KEY_USER_ID)
                if (it.isSuccessful && it.result != null) {
                    val usersList: ArrayList<Users> = ArrayList()
                    for (queryDocumentSnapshot in it.result) {
                        if (currentUserId.equals(queryDocumentSnapshot.id)) {
                            continue
                        }
                        val users = Users()
                        users.name = queryDocumentSnapshot.getString(Constants.KEY_USERS.KEY_NAME).toString()
                        users.email = queryDocumentSnapshot.getString(Constants.KEY_USERS.KEY_EMAIL).toString()
                        users.image = queryDocumentSnapshot.getString(Constants.KEY_USERS.KEY_IMAGE).toString()
                        users.token = queryDocumentSnapshot.getString(Constants.KEY_USERS.KEY_FCM_TOKEN).toString()
                        users.id = queryDocumentSnapshot.id
                        usersList.add(users)
                    }
                    when (usersList.size > 0) {
                        true -> callback(usersList, StatusResponse(isSuccess = true))
                        false -> callback(ArrayList(), StatusResponse(isSuccess = false))
                    }
                } else {
                    callback(ArrayList(), StatusResponse(isSuccess = false))
                }
            }
    }
}