package com.imdvlpr.chatapp.Activity.Auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.imdvlpr.chatapp.R
import com.imdvlpr.chatapp.Shared.UI.InputView
import com.imdvlpr.chatapp.databinding.ActivityLoginBinding

class LoginView : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    private fun initView() {
        binding.txtUsername.apply {
            setTitle(getString(R.string.login_username))
            setHint(getString(R.string.login_username_hint))
            setVisibleIcon(true, R.drawable.ic_user)
            setInputType(InputView.TYPE.EMAIL)
            setImeOption(InputView.ACTION.DONE)
        }

        binding.txtPassword.apply {
            setTitle(getString(R.string.login_password))
            setHint(getString(R.string.login_password_hint))
            setVisibleIcon(true, R.drawable.ic_lock)
            setInputType(InputView.TYPE.PASSWORD)
            setImeOption(InputView.ACTION.DONE)
        }
    }
}