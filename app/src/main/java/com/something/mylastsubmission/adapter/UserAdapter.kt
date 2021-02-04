package com.something.mylastsubmission.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.something.mylastsubmission.R
import com.something.mylastsubmission.entity.User
import com.something.mylastsubmission.ui.DetailActivity
import kotlinx.android.synthetic.main.list_item_user.view.*

class UserAdapter(private val listUser: ArrayList<User>) : RecyclerView.Adapter<UserAdapter.ListViewHolder>(){

    fun setData(items: ArrayList<User>){
        listUser.clear()
        listUser.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.list_item_user, viewGroup, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int = listUser.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) = holder.bind(listUser[position])

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(user: User){
            with(itemView){
                Glide.with(itemView.context)
                        .load(user.photo)
                        .apply(RequestOptions().override(50, 50))
                        .into(img_user)
                tv_username.text = user.username
                tv_url.text = user.url

                itemView.setOnClickListener(CustomOnItemClickListener(object : CustomOnItemClickListener.OnItemClickCallback {
                    override fun onItemClicked(v: View) {
                        val moveDetail = Intent(itemView.context, DetailActivity::class.java)
                        moveDetail.putExtra(DetailActivity.KEY_USER, user)
                        itemView.context.startActivity(moveDetail)
                    }
                }))
            }
        }
    }
}