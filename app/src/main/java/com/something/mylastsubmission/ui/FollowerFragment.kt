package com.something.mylastsubmission.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.something.mylastsubmission.R
import com.something.mylastsubmission.adapter.FollowerAdapter
import com.something.mylastsubmission.entity.User
import com.something.mylastsubmission.model.FollowerViewModel
import kotlinx.android.synthetic.main.fragment_follower.*

class FollowerFragment : Fragment() {

    private val listUser = ArrayList<User>()
    private lateinit var adapterFollower: FollowerAdapter
    private lateinit var followerViewModel: FollowerViewModel

    companion object {
        const val ARG_USERNAME = "username"
        val TAG: String = FollowerFragment::class.java.simpleName
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_follower, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv_follower.setHasFixedSize(true)

        followerViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(FollowerViewModel::class.java)
        showRecyclerFollowers()
        followerViewModel.getListFollowerData().observe(viewLifecycleOwner, Observer { listItems ->
            if (listItems != null){
                adapterFollower.setData(listItems)
                pbFollowers.visibility = View.GONE
            }
        })
        if (arguments != null){
            val username = arguments?.getString(ARG_USERNAME)
            username?.let { followerViewModel.setDataFollower(it) }
            pbFollowers.visibility = View.INVISIBLE
        }

    }

    private fun showRecyclerFollowers() {
        rv_follower.layoutManager = LinearLayoutManager(activity)
        adapterFollower = FollowerAdapter(listUser)
        adapterFollower.notifyDataSetChanged()
        rv_follower.adapter = adapterFollower
    }
}