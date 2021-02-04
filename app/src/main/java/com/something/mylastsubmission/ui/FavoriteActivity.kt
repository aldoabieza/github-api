package com.something.mylastsubmission.ui

import android.content.Intent
import android.database.ContentObserver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.something.mylastsubmission.R
import com.something.mylastsubmission.adapter.FavoriteAdapter
import com.something.mylastsubmission.db.UserContract.UserColumns.Companion.CONTENT_URI
import com.something.mylastsubmission.db.UserHelper
import com.something.mylastsubmission.entity.User
import com.something.mylastsubmission.helper.MappingHelper
import kotlinx.android.synthetic.main.activity_favorite.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {

    private val list = ArrayList<User>()
    private lateinit var adapter: FavoriteAdapter
    private var userHelper: UserHelper? = null

    companion object {
        private const val EXTRA_STATE = "extra_state"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        supportActionBar?.title = resources.getString(R.string.favorite)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        rv_favorite.setHasFixedSize(true)
        showRecyclerFavorite()

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        val myObserver = object : ContentObserver(handler){
            override fun onChange(selfChange: Boolean) {
                super.onChange(selfChange)
                loadFavoritesAsync()
            }
        }

        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)

        if(savedInstanceState == null){
            loadFavoritesAsync()
        }else{
            val item = savedInstanceState.getParcelableArrayList<User>(EXTRA_STATE)
            if (item != null){
                adapter.listUser = item
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.listUser)
    }

    private fun loadFavoritesAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            pb_fav.visibility = View.VISIBLE
            val defferedNotes = async(Dispatchers.IO) {
                val cursor = contentResolver.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val users = defferedNotes.await()
            pb_fav.visibility = View.INVISIBLE
            if (users.size > 0){
                adapter.listUser = users
                adapter.notifyDataSetChanged()
            }else{
                Toast.makeText(applicationContext, "Tidak ada data Favorite", Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        userHelper?.close()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_setting, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_settings -> startActivity(Intent(this, SettingsActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun showRecyclerFavorite() {
        rv_favorite.layoutManager = LinearLayoutManager(this)
        adapter = FavoriteAdapter(list)
        adapter.notifyDataSetChanged()
        rv_favorite.adapter = adapter
    }
}