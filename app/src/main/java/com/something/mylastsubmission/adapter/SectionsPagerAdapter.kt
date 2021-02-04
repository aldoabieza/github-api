package com.something.mylastsubmission.adapter

import android.content.Context
import android.os.Bundle
import androidx.annotation.Nullable
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.something.mylastsubmission.R
import com.something.mylastsubmission.ui.FollowerFragment
import com.something.mylastsubmission.ui.FollowingFragment

class SectionsPagerAdapter(private val mContext: Context, fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    @StringRes
    private val tabsTitle = intArrayOf(R.string.followers, R.string.following)
    private var username: String = "username"

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null

        when(position){
            0 -> {
                fragment = FollowerFragment()
                val bundle = Bundle()
                bundle.putString(FollowerFragment.ARG_USERNAME, getName())
                fragment.arguments = bundle
            }

            1 -> {
                fragment = FollowingFragment()
                val bundle = Bundle()
                bundle.putString(FollowingFragment.ARG_USERNAME, getName())
                fragment.arguments = bundle
            }
        }
        return fragment as Fragment
    }

    fun setUsername(user: String){
        username = user
    }

    private fun getName(): String? = username

    override fun getCount(): Int = 2

    @Nullable
    override fun getPageTitle(position: Int): CharSequence? = mContext.resources.getString(tabsTitle[position])

}