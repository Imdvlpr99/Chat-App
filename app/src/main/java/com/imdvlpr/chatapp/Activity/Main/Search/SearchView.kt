package com.imdvlpr.chatapp.Activity.Main.Search

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.imdvlpr.chatapp.Activity.Main.Chat.ChatView
import com.imdvlpr.chatapp.Activity.Main.MainActivity
import com.imdvlpr.chatapp.Fragments.HomeView
import com.imdvlpr.chatapp.Model.StatusResponse
import com.imdvlpr.chatapp.Model.Users
import com.imdvlpr.chatapp.R
import com.imdvlpr.chatapp.Shared.Extension.setVisible
import com.imdvlpr.chatapp.Shared.UI.InputView
import com.imdvlpr.chatapp.databinding.FragmentHomeBinding
import com.imdvlpr.chatapp.databinding.FragmentSearchBinding

class SearchView : Fragment(), SearchInterface {

    companion object {
        fun newInstance(): SearchView {
            val fragment = SearchView()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var binding: FragmentSearchBinding
    private lateinit var presenter: SearchPresenter
    private lateinit var adapter: UsersAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSearchBinding.bind(inflater.inflate(R.layout.fragment_search, container, false))
        onAttach()
        initView()
        return binding.root
    }

    private fun initView() {
        binding.search.apply {
            setTitleVisible(false)
            setHint(getString(R.string.search_bar_hint))
            setVisibleIcon(false)
            setImageAction(true, R.drawable.ic_search)
            setInputType(InputView.TYPE.TEXT)
            setListener(object : InputView.InputListener {
                override fun afterTextChanged() {
                    adapter.search(binding.search.getText())
                }
            })
        }
    }

    override fun onSuccessGetUsers(users: ArrayList<Users>) {
        binding.emptyLayout.setVisible(false)
        binding.usersRecycler.setVisible(true)
        adapter = UsersAdapter(users, object :UsersAdapter.UsersListener {
            override fun onUserClick(item: Users) {
                startActivity(ChatView.newIntent(context as Context, item))
            }
        })
        binding.usersRecycler.adapter = adapter
    }

    override fun onProgress() {
        (activity as MainActivity).showProgress()
    }

    override fun onFinishProgress() {
        (activity as MainActivity).hideProgress()
    }

    override fun onFailed(statusResponse: StatusResponse) {
        binding.emptyLayout.setVisible(true)
        binding.usersRecycler.setVisible(false)
    }

    override fun onAttach() {
        presenter = SearchPresenter(context as Context)
        presenter.onAttach(this)
        presenter.getUserList()
    }
}