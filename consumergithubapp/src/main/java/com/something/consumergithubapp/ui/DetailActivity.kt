package com.something.consumergithubapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.something.consumergithubapp.BuildConfig
import com.something.consumergithubapp.R
import com.something.consumergithubapp.adapter.SectionsPagerAdapter
import com.something.consumergithubapp.entity.User
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_detail.*
import org.json.JSONObject
import java.lang.Exception

class DetailActivity : AppCompatActivity() {

    companion object {
        const val KEY_USER = "key_user"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val user = intent.getParcelableExtra<User>(KEY_USER)
        user?.username?.let { getDataDetail(it) }

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        sectionsPagerAdapter.setUsername(user?.username.toString())
        view_pager.adapter = sectionsPagerAdapter
        tabs.setupWithViewPager(view_pager)
        supportActionBar?.elevation = 0f
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun getDataDetail(usernamed: String) {
        progressBarDetail.visibility = View.VISIBLE
        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/$usernamed"
        val apiKey = BuildConfig.GithubToken
        client.apply {
            addHeader("Authorization", "token $apiKey")
            addHeader("User-Agent", "request")
            get(url, object : AsyncHttpResponseHandler(){
                override fun onSuccess(
                    statusCode: Int,
                    headers: Array<out Header>,
                    responseBody: ByteArray
                ) {
                    progressBarDetail.visibility = View.INVISIBLE
                    val result = String(responseBody)
                    try{
                        Log.d("Berhasil getApi", result)
                        val item = JSONObject(result)

                        val userItems = User()

                        userItems.username = item.getString("login")
                        userItems.name = item.getString("name")
                        userItems.location = item.getString("location")
                        userItems.company = item.getString("company")
                        userItems.repository = item.getString("public_repos")
                        userItems.followers = item.getString("followers")
                        userItems.following = item.getString("following")
                        userItems.photo = item.getString("avatar_url")


                        supportActionBar?.title = userItems.name

                        name_detail.text = userItems.name
                        username_detail.text = userItems.username
                        location_detail.text = userItems.location
                        company_detail.text = userItems.company
                        repos_detail.text = userItems.repository
                        followers_detail.text = userItems.followers
                        following_detail.text = userItems.following
                        Glide.with(this@DetailActivity)
                            .load(userItems.photo)
                            .apply(RequestOptions().override(100, 100))
                            .into(circleImageView)

                    }catch (e: Exception){
                        e.printStackTrace()
                    }
                }

                override fun onFailure(
                    statusCode: Int,
                    headers: Array<out Header>,
                    responseBody: ByteArray,
                    error: Throwable
                ) {
                    progressBarDetail.visibility = View.INVISIBLE
                    Log.d("onFailure", error.message.toString())
                }
            })
        }
    }

}