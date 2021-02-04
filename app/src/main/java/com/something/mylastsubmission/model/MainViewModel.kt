package com.something.mylastsubmission.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.something.mylastsubmission.BuildConfig
import com.something.mylastsubmission.MainActivity
import com.something.mylastsubmission.entity.User
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import java.lang.Exception

class MainViewModel : ViewModel() {

    val listUsers = MutableLiveData<ArrayList<User>>()

    fun setListData(username: String){
        val listUser = ArrayList<User>()
        val client = AsyncHttpClient()
        val url = "https://api.github.com/search/users?q=$username"
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
                    try{
                        val result = String(responseBody)
                        Log.d(MainActivity.TAG, result)
                        val responseObject = JSONObject(result)
                        val items = responseObject.getJSONArray("items")

                        for (i in 0 until items.length()){
                            val item = items.getJSONObject(i)
                            val name = item.getString("login")
                            val link = item.getString("url")
                            val avatar = item.getString("avatar_url")
                            val userItems = User()
                            userItems.username = name
                            userItems.url = link
                            userItems.photo = avatar
                            listUser.add(userItems)
                        }
                        listUsers.postValue(listUser)
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
                    Log.d("onFailure", error.message.toString())
                }
            })
        }
    }

    fun getListData(): LiveData<ArrayList<User>> {
        return listUsers
    }
}