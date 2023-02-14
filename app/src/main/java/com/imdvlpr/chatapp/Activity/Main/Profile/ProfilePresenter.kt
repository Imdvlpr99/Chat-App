package com.imdvlpr.chatapp.Activity.Main.Profile

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.imdvlpr.chatapp.Shared.Extension.DispatchGroup
import com.imdvlpr.chatapp.Shared.Network.FirestoreConnection
import net.ist.btn.shared.base.BasePresenter

class ProfilePresenter(private val context: Context) : BasePresenter<ProfileInterface> {

    private var view: ProfileInterface? = null
    private var API: FirestoreConnection? = null
    private var dispatchGroup: DispatchGroup? = null

    override fun onAttach(view: ProfileInterface) {
        this.view = view
        if (API == null) API = FirestoreConnection(context)
        if (dispatchGroup == null) {
            dispatchGroup = DispatchGroup()
            dispatchGroup?.notify { view.onFinishProgress() }
        }
    }

    fun logout() {
        view?.onProgress()
        dispatchGroup?.enter()

        API?.logout { status ->
            val mainHandler = Handler(Looper.getMainLooper())
            mainHandler.post {
                when (status.isSuccess) {
                    true -> view?.onSuccessLogout()
                    else -> if (!status.isCanceled) view?.onFailed(status)
                }
                dispatchGroup?.leave()
            }
        }
    }

    override fun onDetach() { view = null }
}