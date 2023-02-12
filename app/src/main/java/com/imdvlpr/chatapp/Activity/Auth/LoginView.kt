package com.imdvlpr.chatapp.Activity.Auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import com.imdvlpr.chatapp.Activity.Main.MainActivity
import com.imdvlpr.chatapp.Model.StatusResponse
import com.imdvlpr.chatapp.R
import com.imdvlpr.chatapp.Shared.Base.BaseActivity
import com.imdvlpr.chatapp.Shared.Extension.Constants
import com.imdvlpr.chatapp.Shared.Extension.PreferenceManager
import com.imdvlpr.chatapp.Shared.UI.InputView
import com.imdvlpr.chatapp.databinding.ActivityLoginBinding

class LoginView : BaseActivity(), AuthInterface {

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, LoginView::class.java)
        }
    }
    private lateinit var binding: ActivityLoginBinding
    private lateinit var presenter: AuthPresenter
    private lateinit var preferenceManager: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferenceManager = PreferenceManager(applicationContext)
        initView()
        onAttach()
    }

    private fun initView() {
        if (preferenceManager.getBoolean(Constants.KEY_USERS.KEY_IS_SIGNED_IN)) {
            startActivity(MainActivity.newIntent(this))
            finish()
        }

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

        binding.btnRegister.setOnClickListener {
            startActivity(RegisterView.newIntent(this))
        }

        binding.btnLogin.setOnClickListener {
            if (isValidInput()) {
                presenter.login(binding.txtUsername.getText(), binding.txtPassword.getText())
            }
        }
    }

    private fun isValidInput() : Boolean {
        if (binding.txtUsername.getText().isEmpty()) {
            binding.txtUsername.apply {
                setInfo("Email tidak boleh kosong !")
                setFieldError(true)
            }
            return false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.txtUsername.getText()).matches()) {
            binding.txtUsername.apply {
                setInfo("Email tidak valid !")
                setFieldError(true)
            }
            return false
        } else if (binding.txtPassword.getText().isEmpty()) {
            binding.txtPassword.apply {
                setInfo("Password tidak boleh kosong !")
                setFieldError(true)
            }
            return false
        } else {
            return true
        }
    }

    override fun onSuccessRegister() {}

    override fun onSuccessLogin() {
        startActivity(MainActivity.newIntent(this))
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
        presenter = AuthPresenter(this)
        presenter.onAttach(this)
    }

    override fun onDetach() {
        presenter.onDetach()
    }
}