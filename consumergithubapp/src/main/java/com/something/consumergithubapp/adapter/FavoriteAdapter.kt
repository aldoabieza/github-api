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

class FavoriteAdapter(private val list: ArrayList<User>) : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    var listUser = ArrayList<User>()
        set(listNotes) {
            if (listNotes.size > 0) {
                this.listUser.clear()
            }
            this.listUser.addAll(listNotes)

            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_user, parent, false)
        return FavoriteViewHolder(view)
    }

    override fun getItemCount(): Int = this.listUser.size

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(listUser[position])
    }

    inner class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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
