package com.imdvlpr.chatapp.Activity.Auth

import com.imdvlpr.chatapp.Model.StatusResponse
import net.ist.btn.shared.base.BaseView

interface AuthInterface: BaseView {
    fun onSuccessRegister()

    fun onSuccessLogin()

    fun onProgress()

    fun onFinishProgress()

    fun onFailed(statusResponse: StatusResponse)
}