package com.imdvlpr.chatapp.Activity.Auth

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Patterns
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.imdvlpr.chatapp.Model.StatusResponse
import com.imdvlpr.chatapp.R
import com.imdvlpr.chatapp.Shared.Base.BaseActivity
import com.imdvlpr.chatapp.Shared.Extension.encodedImage
import com.imdvlpr.chatapp.Shared.Extension.setVisible
import com.imdvlpr.chatapp.Shared.UI.InputView
import com.imdvlpr.chatapp.databinding.ActivityRegisterBinding
import java.io.InputStream

class RegisterView : BaseActivity(), AuthInterface {

    companion object {
        fun newIntent(context: Context): Intent{
            return Intent(context, RegisterView::class.java)
        }
    }

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var presenter: AuthPresenter
    private var encodedImage: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        onAttach()
    }

    private fun initView() {
        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
            finish()
        }

        binding.txtName.apply {
            setTitle(getString(R.string.register_full_name))
            setHint(getString(R.string.register_full_name_hint))
            setVisibleIcon(true, R.drawable.ic_user)
            setInputType(InputView.TYPE.TEXT)
            setImeOption(InputView.ACTION.DONE)
            setListener(object : InputView.InputListener {
                override fun afterTextChanged() {
                    setFieldError(false)
                }
            })
        }

        binding.txtEmail.apply {
            setTitle(getString(R.string.register_email))
            setHint(getString(R.string.register_email_hint))
            setVisibleIcon(true, R.drawable.ic_email)
            setInputType(InputView.TYPE.EMAIL)
            setImeOption(InputView.ACTION.DONE)
            setListener(object : InputView.InputListener {
                override fun afterTextChanged() {
                    setFieldError(false)
                }
            })
        }

        binding.txtPassword.apply {
            setTitle(getString(R.string.register_password))
            setHint(getString(R.string.register_password_hint))
            setVisibleIcon(true, R.drawable.ic_lock)
            setInputType(InputView.TYPE.PASSWORD)
            setImeOption(InputView.ACTION.DONE)
            setListener(object : InputView.InputListener {
                override fun afterTextChanged() {
                    setFieldError(false)
                }
            })
        }

        binding.txtConfirmPassword.apply {
            setTitle(getString(R.string.register_confirm_password))
            setHint(getString(R.string.register_password_hint))
            setVisibleIcon(true, R.drawable.ic_lock)
            setInputType(InputView.TYPE.PASSWORD)
            setImeOption(InputView.ACTION.DONE)
            setListener(object : InputView.InputListener {
                override fun afterTextChanged() {
                    setFieldError(false)
                }
            })
        }

        binding.btnRegister.setOnClickListener {
            if (isValidInput()) {
                presenter.registerUser(
                    binding.txtName.getText(),
                    binding.txtEmail.getText(),
                    binding.txtPassword.getText(),
                    encodedImage.toString()
                )
            }
        }

        binding.pickImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            pickImages.launch(intent)
        }
    }

    private var pickImages = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            if (result.data != null) {
                val imageUri: Uri? = result.data!!.data
                val inputStream: InputStream? = imageUri?.let { contentResolver.openInputStream(it) }
                val bitmap: Bitmap = BitmapFactory.decodeStream(inputStream)
                binding.userImage.setImageBitmap(bitmap)
                encodedImage = encodedImage(bitmap)
                binding.pickImage.setVisible(false)
            }
        }
    }

    fun isValidInput(): Boolean {
        if (encodedImage == null) {
            Toast.makeText(this, "Foto Profile tidak boleh kosong", Toast.LENGTH_LONG).show()
            return false
        } else if (binding.txtName.getText().isEmpty()) {
            binding.txtName.apply {
                setInfo("Nama Lengkap tidak boleh kosong !")
                setFieldError(true)
            }
            return false
        } else if (binding.txtEmail.getText().isEmpty()) {
            binding.txtEmail.apply {
                setInfo("Email tidak boleh kosong !")
                setFieldError(true)
            }
            return false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.txtEmail.getText()).matches()) {
            binding.txtEmail.apply {
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
        } else if (binding.txtConfirmPassword.getText().isEmpty()) {
            binding.txtConfirmPassword.apply {
                setInfo("Konfirmasi Password tidak boleh kosong !")
                setFieldError(true)
            }
            return false
        } else if (binding.txtPassword.getText() != binding.txtConfirmPassword.getText()) {
            binding.txtConfirmPassword.apply {
                setInfo("Password tidak sesuai !")
                setFieldError(true)
            }
            return false
        } else {
            return true
        }
    }

    override fun onSuccessRegister() {
        startActivity(LoginView.newIntent(this))
    }

    override fun onSuccessLogin() {}
    override fun onSuccessUpdateToken() {}

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