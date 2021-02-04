package com.something.mylastsubmission.ui

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.something.mylastsubmission.BuildConfig
import com.something.mylastsubmission.R
import com.something.mylastsubmission.adapter.SectionsPagerAdapter
import com.something.mylastsubmission.db.UserContract
import com.something.mylastsubmission.db.UserContract.UserColumns.Companion.CONTENT_URI
import com.something.mylastsubmission.db.UserHelper
import com.something.mylastsubmission.entity.User
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_detail.*
import org.json.JSONObject
import java.lang.Exception

class DetailActivity : AppCompatActivity() {

    private var isFavorite = false
    private lateinit var userHelper: UserHelper

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

        setStatusFavorite(isFavorite)

        userHelper = UserHelper.getInstance(applicationContext)
        userHelper.open()

        if (userHelper.check(user?.username.toString())) {
            isFavorite = true
            setStatusFavorite(isFavorite)
        }

        fab.setOnClickListener {
            if (!isFavorite){
                isFavorite = !isFavorite

                val values = ContentValues().apply {
                    put(UserContract.UserColumns.USERNAME, user?.username)
                    put(UserContract.UserColumns.URL, user?.url)
                    put(UserContract.UserColumns.PHOTO, user?.photo)
                }

                contentResolver.insert(CONTENT_URI, values)
                setStatusFavorite(isFavorite)
                Toast.makeText(this, "${user?.username} have been added to Favorite", Toast.LENGTH_SHORT).show()

            } else {

                isFavorite = false
                userHelper.deleteByUsername(user?.username.toString())
                setStatusFavorite(isFavorite)
                Toast.makeText(this, "${user?.username} have been delete from Favorite", Toast.LENGTH_SHORT).show()

            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.favorite -> startActivity(Intent(this, FavoriteActivity::class.java))
            R.id.settings -> startActivity(Intent(this, SettingsActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun setStatusFavorite(statusFavorite: Boolean){
        if (statusFavorite){
            fab.setImageResource(R.drawable.ic_baseline_favorite_24)
        }else{
            fab.setImageResource(R.drawable.ic_baseline_favorite_border_24)
        }
    }

    private fun getDataDetail(usernamed: String) {
        progressBarDetail.visibility = View.VISIBLE
        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/$usernamed"
        val tokenApi = BuildConfig.GithubToken
        client.apply {
            addHeader("Authorization", "token $tokenApi")
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