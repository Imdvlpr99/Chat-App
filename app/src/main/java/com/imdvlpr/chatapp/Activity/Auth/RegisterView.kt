package com.imdvlpr.chatapp.Activity.Auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.imdvlpr.chatapp.R
import com.imdvlpr.chatapp.databinding.ActivityRegisterBinding

class RegisterView : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    private fun initView() {

    }
}