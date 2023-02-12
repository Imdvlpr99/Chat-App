package com.imdvlpr.chatapp.Activity.Auth

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.imdvlpr.chatapp.Shared.Extension.DispatchGroup
import com.imdvlpr.chatapp.Shared.Network.FirestoreConnection
import net.ist.btn.shared.base.BasePresenter

class AuthPresenter(private val context: Context) : BasePresenter<AuthInterface> {

    private var view: AuthInterface? = null
    private var API: FirestoreConnection? = null
    private var dispatchGroup: DispatchGroup? = null

    override fun onAttach(view: AuthInterface) {
        this.view = view
        if (API == null) API = FirestoreConnection(context)
        if (dispatchGroup == null) {
            dispatchGroup = DispatchGroup()
            dispatchGroup?.notify { view.onFinishProgress() }
        }
    }

    fun registerUser(name: String, email: String, password: String, userImage: String) {
        view?.onProgress()
        dispatchGroup?.enter()

        API?.registerUser(name, email, password, userImage) { status ->
            val mainHandler = Handler(Looper.getMainLooper())
            mainHandler.post {
                when (status.isSuccess) {
                    true -> view?.onSuccessRegister()
                    else -> if (!status.isCanceled) view?.onFailed(status)
                }
                dispatchGroup?.leave()
            }
        }
    }

    fun login(email: String, password: String) {
        view?.onProgress()
        dispatchGroup?.enter()

        API?.login(email, password) { status ->
            val mainHandler = Handler(Looper.getMainLooper())
            mainHandler.post {
                when (status.isSuccess) {
                    true -> view?.onSuccessLogin()
                    else -> if (!status.isCanceled) view?.onFailed(status)
                }
                dispatchGroup?.leave()
            }
        }
    }

    override fun onDetach() { view = null }
}