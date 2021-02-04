package com.something.mylastsubmission.helper

import android.database.Cursor
import com.something.mylastsubmission.db.UserContract
import com.something.mylastsubmission.entity.User

object MappingHelper {

    fun mapCursorToArrayList(userCursor: Cursor?) : ArrayList<User>{
        val userList = ArrayList<User>()

        userCursor?.apply {
            while (moveToNext()){
                val id = getInt(getColumnIndexOrThrow(UserContract.UserColumns._ID))
                val username = getString(getColumnIndexOrThrow(UserContract.UserColumns.USERNAME))
                val url = getString(getColumnIndexOrThrow(UserContract.UserColumns.URL))
                val photo = getString(getColumnIndexOrThrow(UserContract.UserColumns.PHOTO))
                userList.add(User(id, username, url, photo))
            }
        }

        return userList

    }

}