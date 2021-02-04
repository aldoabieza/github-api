package com.something.mylastsubmission

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.something.mylastsubmission.adapter.UserAdapter
import com.something.mylastsubmission.entity.User
import com.something.mylastsubmission.model.MainViewModel
import com.something.mylastsubmission.ui.FavoriteActivity
import com.something.mylastsubmission.ui.SettingsActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val list = ArrayList<User>()
    private lateinit var userAdapter: UserAdapter
    private lateinit var mainViewModel: MainViewModel

    companion object {
        val TAG: String = MainActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rv_user.setHasFixedSize(true)
        showRecyclerList()
        searchList()

        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)

        mainViewModel.getListData().observe(this, Observer { listItems ->
            if (listItems != null){
                userAdapter.setData(listItems)
                progressBar.visibility = View.GONE
            }
        })
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

    private fun searchList() {
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        sv_user.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        sv_user.queryHint = resources.getString(R.string.search_hint)
        sv_user.setOnQueryTextListener(object : SearchView.OnQueryTextListener{

            override fun onQueryTextSubmit(query: String): Boolean {
                mainViewModel.setListData(query)
                progressBar.visibility = View.VISIBLE
                return true
            }
            override fun onQueryTextChange(resText: String): Boolean {
                return false
            }
        })
    }

    private fun showRecyclerList() {
        rv_user.layoutManager = LinearLayoutManager(this)
        userAdapter = UserAdapter(list)
        userAdapter.notifyDataSetChanged()
        rv_user.adapter = userAdapter
    }
}