package com.something.consumergithubapp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.something.consumergithubapp.R
import com.something.consumergithubapp.entity.User
import com.something.consumergithubapp.ui.DetailActivity
import kotlinx.android.synthetic.main.list_item_user.view.*

class FollowerAdapter (private val listUser: ArrayList<User>) : RecyclerView.Adapter<FollowerAdapter.CardViewHolder>() {

    fun setData(items: ArrayList<User>){
        listUser.clear()
        listUser.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): CardViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.list_item_user, viewGroup, false)
        return CardViewHolder(view)
    }

    override fun getItemCount(): Int =  listUser.size

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) = holder.bind(listUser[position])

    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(user: User){
            with(itemView){
                Glide.with(itemView.context)
                        .load(user.photo)
                        .apply(RequestOptions().override(350, 350))
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