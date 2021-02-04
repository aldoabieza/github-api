package com.something.mylastsubmission.db

import android.net.Uri
import android.provider.BaseColumns

object UserContract {

    const val AUTHORITY = "com.something.mylastsubmission"
    const val SCHEME = "content"

    internal class UserColumns: BaseColumns {

        companion object {
            const val TABLE_NAME = "favorite"
            const val _ID = "_id"
            const val USERNAME = "username"
            const val URL = "url"
            const val PHOTO = "photo"

            val CONTENT_URI : Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()
        }
    }
}

//username, name, repository, followers, following, location, company, url, photo