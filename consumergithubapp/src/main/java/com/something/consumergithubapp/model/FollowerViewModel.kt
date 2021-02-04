package com.something.consumergithubapp.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.something.consumergithubapp.BuildConfig
import com.something.consumergithubapp.ui.FollowerFragment
import com.something.consumergithubapp.entity.User
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import java.lang.Exception

class FollowerViewModel : ViewModel(){

    val listUsers = MutableLiveData<ArrayList<User>>()

    fun setDataFollower(username: String) {

        val listUser = ArrayList<User>()
        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/$username/followers"
        val apiKey = BuildConfig.GithubToken
        client.apply {
            addHeader("Authorization", "token $apiKey")
            addHeader("User-Agent", "request")
            get(url, object : AsyncHttpResponseHandler(){
                override fun onSuccess(statusCode: Int, headers: Array<out Header>, responseBody: ByteArray) {
                    val result = String(responseBody)
                    Log.d(FollowerFragment.TAG, result)

                    try {
                        val list = JSONArray(result)
                        for (i in 0 until list.length()){
                            val listItem = list.getJSONObject(i)
                            Log.d(FollowerFragment.TAG, "Sukses get JSON Objek")
                            val user = User()
                            user.username = listItem.getString("login")
                            user.url = listItem.getString("url")
                            user.photo = listItem.getString("avatar_url")
                            listUser.add(user)
                        }
                        listUsers.postValue(listUser)
                    }catch (e: Exception){
                        Log.d("Exception", e.message.toString())
                    }
                }

                override fun onFailure(statusCode: Int, headers: Array<out Header>, responseBody: ByteArray, error: Throwable) {
                    Log.d("Failed Connect", error.message.toString())
                }
            })
        }
    }

    fun getListFollowerData(): LiveData<ArrayList<User>> {
        return listUsers
    }

}