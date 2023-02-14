package com.imdvlpr.chatapp.Activity.Main.Search

import com.imdvlpr.chatapp.Model.StatusResponse
import com.imdvlpr.chatapp.Model.Users
import net.ist.btn.shared.base.BaseView

interface SearchInterface: BaseView {

    fun onSuccessGetUsers(users: ArrayList<Users>)

    fun onProgress()

    fun onFinishProgress()

    fun onFailed(statusResponse: StatusResponse)
}