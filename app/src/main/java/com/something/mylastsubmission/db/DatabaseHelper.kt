package com.something.mylastsubmission.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.something.mylastsubmission.db.UserContract.UserColumns.Companion.TABLE_NAME

internal class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "dbuser"
        private const val DATABASE_VERSION = 1
        private const val SQL_CREATE_TABLE_USER = "CREATE TABLE $TABLE_NAME" +
                "(${UserContract.UserColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                "${UserContract.UserColumns.USERNAME} TEXT NOT NULL," +
                "${UserContract.UserColumns.URL} TEXT NOT NULL," +
                "${UserContract.UserColumns.PHOTO} TEXT NOT NULL)"

        private const val SQL_DROP_TABLE_USER = "DROP TABLE IF EXISTS $TABLE_NAME"

    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_USER)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DROP_TABLE_USER)
        onCreate(db)
    }
}