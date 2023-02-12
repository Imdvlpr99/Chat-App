package com.imdvlpr.chatapp.Activity.Main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.imdvlpr.chatapp.Shared.Base.BaseActivity
import com.imdvlpr.chatapp.Shared.Extension.PreferenceManager
import com.imdvlpr.chatapp.databinding.ActivityMainBinding

class MainActivity : BaseActivity() {

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}