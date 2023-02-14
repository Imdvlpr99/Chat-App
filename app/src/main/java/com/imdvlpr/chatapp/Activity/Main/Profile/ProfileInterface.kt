package com.imdvlpr.chatapp.Activity.Main.Profile

import com.imdvlpr.chatapp.Model.StatusResponse
import net.ist.btn.shared.base.BaseView

interface ProfileInterface: BaseView {

    fun onSuccessLogout()

    fun onProgress()

    fun onFinishProgress()

    fun onFailed(statusResponse: StatusResponse)
}