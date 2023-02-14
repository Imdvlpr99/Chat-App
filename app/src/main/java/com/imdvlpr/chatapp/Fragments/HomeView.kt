package com.imdvlpr.chatapp.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.imdvlpr.chatapp.R
import com.imdvlpr.chatapp.databinding.FragmentHomeBinding

class HomeView : Fragment() {

    companion object {
        fun newInstance(): HomeView {
            val fragment = HomeView()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentHomeBinding.bind(inflater.inflate(R.layout.fragment_home, container, false))
        initView()
        return binding.root
    }

    private fun initView() {

    }
}