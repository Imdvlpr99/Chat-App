package com.imdvlpr.chatapp.Activity.Main.Profile

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import com.imdvlpr.chatapp.Activity.Auth.LoginView
import com.imdvlpr.chatapp.Model.StatusResponse
import com.imdvlpr.chatapp.R
import com.imdvlpr.chatapp.Shared.Base.BaseActivity
import com.imdvlpr.chatapp.Shared.Extension.Constants
import com.imdvlpr.chatapp.Shared.Extension.PreferenceManager
import com.imdvlpr.chatapp.Shared.UI.CustomTextView
import com.imdvlpr.chatapp.databinding.ActivityProfileBinding

class ProfileView : BaseActivity(), ProfileInterface {

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, ProfileView::class.java)
        }
    }

    private lateinit var binding: ActivityProfileBinding
    private lateinit var presenter: ProfilePresenter
    private lateinit var preferenceManager: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferenceManager = PreferenceManager(applicationContext)
        onAttach()
        initView()
    }

    private fun initView() {
        binding.txtName.apply {
            setTitle(true, getString(R.string.profile_name))
            setTextViewIcon(true, R.drawable.ic_user)
            setIconAction(true, R.drawable.ic_edit)
            setListener(object : CustomTextView.TextViewListener {
                override fun onActionClickListener() {

                }
            })
        }

        binding.txtEmail.apply {
            setTitle(true, getString(R.string.profile_email))
            setTextViewIcon(true, R.drawable.ic_email)
            setIconAction(false, R.drawable.ic_edit)
        }

        binding.txtPhone.apply {
            setTitle(true, getString(R.string.profile_phone))
            setTextViewIcon(true, R.drawable.ic_phone)
            setIconAction(true, R.drawable.ic_edit)
            setListener(object : CustomTextView.TextViewListener {
                override fun onActionClickListener() {

                }
            })
        }

        binding.btnLogout.setOnClickListener { presenter.logout() }

        val bytes = android.util.Base64.decode(preferenceManager.getString(Constants.KEY_USERS.KEY_IMAGE), android.util.Base64.DEFAULT)
        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        binding.userImage.setImageBitmap(bitmap)
        binding.txtName.setValue(preferenceManager.getString(Constants.KEY_USERS.KEY_NAME).toString())
        binding.txtEmail.setValue(preferenceManager.getString(Constants.KEY_USERS.KEY_EMAIL).toString())
        binding.txtPhone.setValue(
            when(preferenceManager.getString(Constants.KEY_USERS.KEY_PHONE).toString().equals(null)) {
                true -> "-"
                false -> preferenceManager.getString(Constants.KEY_USERS.KEY_PHONE).toString()
        })
    }

    override fun onSuccessLogout() {
        startActivity(LoginView.newIntent(this))
        finish()
    }

    override fun onProgress() {
        if (!isFinishing) showProgress()
    }

    override fun onFinishProgress() {
        if (!isFinishing) hideProgress()
    }

    override fun onFailed(statusResponse: StatusResponse) {
        if (!isFinishing) {}
    }

    override fun onAttach() {
        presenter = ProfilePresenter(this)
        presenter.onAttach(this)
    }

    override fun onDetach() {
        presenter.onDetach()
    }
}