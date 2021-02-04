package com.something.mylastsubmission.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.something.mylastsubmission.db.UserContract
import com.something.mylastsubmission.db.UserContract.UserColumns.Companion.CONTENT_URI
import com.something.mylastsubmission.db.UserHelper

class FavoriteProvider : ContentProvider() {
    companion object {
        private const val USER = 1
        private const val USER_ID = 2

        private lateinit var userHelper: UserHelper
        private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)

        init {
            uriMatcher.addURI(
                UserContract.AUTHORITY,
                UserContract.UserColumns.TABLE_NAME, USER)
            uriMatcher.addURI(UserContract.AUTHORITY, "${UserContract.UserColumns.TABLE_NAME}/#", USER_ID)
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val added : Long = when(USER) {
            uriMatcher.match(uri) -> userHelper.insert(values)
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return Uri.parse("$CONTENT_URI/$added")
    }

    override fun query(
        uri: Uri,
        strings: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? {
        return when(uriMatcher.match(uri)) {
            USER -> userHelper.queryAll()
            USER_ID -> userHelper.queryById(uri.lastPathSegment.toString())
            else -> null
        }
    }

    override fun onCreate(): Boolean {
        userHelper = UserHelper.getInstance(context as Context)
        userHelper.open()

        return true
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        val update : Int = when(USER_ID) {
            uriMatcher.match(uri) -> userHelper.update(uri.lastPathSegment.toString(), values)
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return update
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val delete = when(USER_ID) {
            uriMatcher.match(uri) -> userHelper.deleteByUsername(uri.lastPathSegment.toString())
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return delete
    }

    override fun getType(uri: Uri): String? {
        return null
    }
}