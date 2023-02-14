package com.imdvlpr.chatapp.Activity.Main

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.google.firebase.messaging.FirebaseMessaging
import com.imdvlpr.chatapp.Activity.Auth.AuthInterface
import com.imdvlpr.chatapp.Activity.Auth.AuthPresenter
import com.imdvlpr.chatapp.Activity.Main.Profile.ProfileView
import com.imdvlpr.chatapp.Activity.Main.Search.SearchView
import com.imdvlpr.chatapp.Fragments.HomeView
import com.imdvlpr.chatapp.Fragments.StatusView
import com.imdvlpr.chatapp.Model.StatusResponse
import com.imdvlpr.chatapp.R
import com.imdvlpr.chatapp.Shared.Base.BaseActivity
import com.imdvlpr.chatapp.Shared.Extension.Constants
import com.imdvlpr.chatapp.Shared.Extension.PreferenceManager
import com.imdvlpr.chatapp.Shared.Extension.ViewPagerAdapter
import com.imdvlpr.chatapp.Shared.Extension.hideSoftKeyboard
import com.imdvlpr.chatapp.databinding.ActivityMainBinding
import nl.joery.animatedbottombar.AnimatedBottomBar

class MainActivity : BaseActivity(), AuthInterface {

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private lateinit var fragmentHome: HomeView
    private lateinit var fragmentStatus: StatusView
    private lateinit var fragmentSearch: SearchView
    private lateinit var preferenceManager: PreferenceManager
    private lateinit var presenter: AuthPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferenceManager = PreferenceManager(applicationContext)
        onAttach()
        initView()
        setUpMenu()
    }

    private fun initView() {
        val bytes = android.util.Base64.decode(preferenceManager.getString(Constants.KEY_USERS.KEY_IMAGE), android.util.Base64.DEFAULT)
        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        binding.userImage.setImageBitmap(bitmap)
        binding.userImage.setOnClickListener {
            startActivity(ProfileView.newIntent(this))
        }

        binding.bottomBar.setupWithViewPager(binding.viewPager)
        binding.bottomBar.setOnTabSelectListener(object : AnimatedBottomBar.OnTabSelectListener {
            override fun onTabSelected(
                lastIndex: Int,
                lastTab: AnimatedBottomBar.Tab?,
                newIndex: Int,
                newTab: AnimatedBottomBar.Tab
            ) {
                loadFragment(newIndex)
            }
            override fun onTabReselected(index: Int, tab: AnimatedBottomBar.Tab) {
            }
        })
    }

    private fun loadFragment(itemId: Int) {
        when (itemId) {
            R.id.tab_home -> {
                setSelected(0)
            }
            R.id.tab_search -> {
                setSelected(1)
            }
            R.id.tab_status -> {
                setSelected(2)
            }
        }
    }

    private fun setSelected(position: Int) {
        hideSoftKeyboard()
        when(position) {
            0 -> binding.viewPager.currentItem = 0
            1 -> binding.viewPager.currentItem = 1
            2 -> binding.viewPager.currentItem = 2
        }
    }

    private fun setUpMenu() {
        fragmentHome = HomeView.newInstance()
        fragmentStatus = StatusView.newInstance()
        fragmentSearch = SearchView.newInstance()

        binding.viewPager.offscreenPageLimit = 3
        viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)
        viewPagerAdapter.addFragment(fragmentHome)
        viewPagerAdapter.addFragment(fragmentSearch)
        viewPagerAdapter.addFragment(fragmentStatus)
        binding.viewPager.adapter = viewPagerAdapter
        binding.viewPager.addOnPageChangeListener(onPageChangeListener)
        binding.viewPager.currentItem = 0
        binding.bottomBar.selectTabAt(0)
    }

    private val onPageChangeListener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
        override fun onPageScrollStateChanged(state: Int) {}
        override fun onPageSelected(position: Int) {
            when(position) {
                0 -> binding.title.text = getString(R.string.home_title)
                1 -> binding.title.text = getString(R.string.home_search_title)
                2 -> binding.title.text = getString(R.string.home_status_title)
            }
        }
    }

    private fun getToken() {
        FirebaseMessaging.getInstance().token.addOnSuccessListener { token ->
            run {
                presenter.updateToken(token)
            }
        }
    }

    override fun onSuccessUpdateToken() {}
    override fun onSuccessRegister() {}

    override fun onSuccessLogin() {}

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
        getToken()
    }

    override fun onDetach() {
        presenter.onDetach()
    }
}