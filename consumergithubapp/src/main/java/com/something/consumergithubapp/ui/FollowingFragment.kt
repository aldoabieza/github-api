package com.something.consumergithubapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.something.consumergithubapp.R
import com.something.consumergithubapp.adapter.FollowerAdapter
import com.something.consumergithubapp.entity.User
import com.something.consumergithubapp.model.FollowingViewModel
import kotlinx.android.synthetic.main.fragment_following.*

class FollowingFragment : Fragment() {

    private val list = ArrayList<User>()
    private lateinit var adapter: FollowerAdapter
    private lateinit var followingViewModel: FollowingViewModel

    companion object {
        const val ARG_USERNAME = "username"
        val TAG: String = FollowingFragment::class.java.simpleName
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_following, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv_following.setHasFixedSize(true)

        followingViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(FollowingViewModel::class.java)
        showRecyclerFollowing()

        followingViewModel.getListFollowingData().observe(viewLifecycleOwner, Observer { listItems ->
            if (listItems != null){
                adapter.setData(listItems)
                pbFollowing.visibility = View.GONE
            }
        })

        if (arguments != null){
            val username = arguments?.getString(ARG_USERNAME)
            username?.let { followingViewModel.setDataFollowing(it) }
            pbFollowing.visibility = View.INVISIBLE
        }
    }

    private fun showRecyclerFollowing() {
        rv_following.layoutManager = LinearLayoutManager(activity)
        adapter = FollowerAdapter(list)
        adapter.notifyDataSetChanged()
        rv_following.adapter = adapter
    }
}