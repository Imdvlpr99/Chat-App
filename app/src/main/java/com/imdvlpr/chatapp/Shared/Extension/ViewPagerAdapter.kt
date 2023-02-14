package com.imdvlpr.chatapp.Shared.Extension

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter


class ViewPagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {
    private val listFragments = mutableListOf<Fragment>()

    override fun getItem(position: Int): Fragment {
        return listFragments[position]
    }

    override fun getCount(): Int {
        return listFragments.size
    }

    fun addFragment(fragment: Fragment) {
        listFragments.add(fragment)
    }
}