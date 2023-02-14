package com.imdvlpr.chatapp.Activity.Main.Search

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.imdvlpr.chatapp.Shared.Extension.DispatchGroup
import com.imdvlpr.chatapp.Shared.Network.FirestoreConnection
import net.ist.btn.shared.base.BasePresenter

class SearchPresenter(private val context: Context) : BasePresenter<SearchInterface> {

    private var view: SearchInterface? = null
    private var API: FirestoreConnection? = null
    private var dispatchGroup: DispatchGroup? = null

    override fun onAttach(view: SearchInterface) {
        this.view = view
        if (API == null) API = FirestoreConnection(context)
        if (dispatchGroup == null) {
            dispatchGroup = DispatchGroup()
            dispatchGroup?.notify { view.onFinishProgress() }
        }
    }

    fun getUserList() {
        view?.onProgress()
        dispatchGroup?.enter()

        API?.getUserList() { array, status ->
            val mainHandler = Handler(Looper.getMainLooper())
            mainHandler.post {
                when (status.isSuccess) {
                    true -> view?.onSuccessGetUsers(array)
                    else -> if (!status.isCanceled) view?.onFailed(status)
                }
                dispatchGroup?.leave()
            }
        }
    }

    override fun onDetach() {view = null}
}