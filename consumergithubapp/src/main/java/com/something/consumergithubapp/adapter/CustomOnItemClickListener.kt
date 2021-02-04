package com.something.consumergithubapp.adapter

import android.view.View

class CustomOnItemClickListener (private val onItemClickCallback: OnItemClickCallback) : View.OnClickListener {
    interface OnItemClickCallback {
        fun onItemClicked(v: View)
    }

    override fun onClick(v: View) {
        onItemClickCallback.onItemClicked(v)
    }
}