package com.imdvlpr.chatapp.Shared.Network

import android.content.Context
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.imdvlpr.chatapp.Model.StatusResponse
import com.imdvlpr.chatapp.Shared.Extension.Constants
import com.imdvlpr.chatapp.Shared.Extension.PreferenceManager

class FirestoreConnection(val context: Context) {

    private var preferenceManager: PreferenceManager = PreferenceManager(context)
    private val database = FirebaseFirestore.getInstance()

    fun registerUser(name: String, email: String, password: String, userImage: String, callback: (StatusResponse) -> Unit) {
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
                callback(StatusResponse(isSuccess = false))
            }

    }

    fun login(email: String, password: String, callback: (StatusResponse) -> Unit) {
        database.collection(Constants.KEY_USERS.KEY_COLLECTION_USERS)
            .whereEqualTo(Constants.KEY_USERS.KEY_EMAIL, email)
            .whereEqualTo(Constants.KEY_USERS.KEY_PASSWORD, password)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val documentSnapshot: DocumentSnapshot = result.documents[0]
                    preferenceManager.putBoolean(Constants.KEY_USERS.KEY_IS_SIGNED_IN, true)
                    preferenceManager.putString(Constants.KEY_USERS.KEY_USER_ID, documentSnapshot.id)
                    preferenceManager.putString(Constants.KEY_USERS.KEY_NAME, documentSnapshot.getString(Constants.KEY_USERS.KEY_NAME).toString())
                    preferenceManager.putString(Constants.KEY_USERS.KEY_IMAGE, documentSnapshot.getString(Constants.KEY_USERS.KEY_IMAGE).toString())
                    callback(StatusResponse(isSuccess = true))
                }
            }
            .addOnFailureListener {
                callback(StatusResponse(isSuccess = false))
            }
    }
}