package com.something.mylastsubmission.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.something.mylastsubmission.BuildConfig
import com.something.mylastsubmission.entity.User
import com.something.mylastsubmission.ui.FollowingFragment
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import java.lang.Exception

class FollowingViewModel : ViewModel() {

    val listUsers = MutableLiveData<ArrayList<User>>()

    fun setDataFollowing(username: String) {
        val listUser = ArrayList<User>()
        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/$username/following"
        val tokenApi = BuildConfig.GithubToken
        client.apply {
            addHeader("Authorization", "token $tokenApi")
            addHeader("User-Agent", "request")
            get(url, object : AsyncHttpResponseHandler(){
                override fun onSuccess(statusCode: Int, headers: Array<out Header>, responseBody: ByteArray) {
                    try {
                        val result = String(responseBody)
                        Log.d(FollowingFragment.TAG, result)
                        val list = JSONArray(result)
                        for (i in 0 until list.length()){
                            val objekUser = list.getJSONObject(i)
                            val user = User()
                            user.username = objekUser.getString("login")
                            user.url = objekUser.getString("url")
                            user.photo = objekUser.getString("avatar_url")
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

    fun getListFollowingData(): LiveData<ArrayList<User>>{
        return listUsers
    }

}