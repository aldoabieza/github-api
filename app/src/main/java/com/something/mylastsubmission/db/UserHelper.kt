package com.something.mylastsubmission.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns
import android.util.Log
import androidx.constraintlayout.widget.Constraints
import com.something.mylastsubmission.db.UserContract.UserColumns.Companion.TABLE_NAME
import com.something.mylastsubmission.db.UserContract.UserColumns.Companion.USERNAME
import com.something.mylastsubmission.db.UserContract.UserColumns.Companion._ID
import java.sql.SQLException

class UserHelper(ctx: Context){

    companion object {
        private const val DATABASE_TABLE = TABLE_NAME
        private lateinit var databaseHelper: DatabaseHelper
        private lateinit var database: SQLiteDatabase
        private var INSTANCE: UserHelper? = null

        fun getInstance(ctx: Context): UserHelper =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: UserHelper(ctx)
                }
    }

    init {
        databaseHelper = DatabaseHelper(ctx)
    }

    @Throws(SQLException::class)
    fun open(){
        database = databaseHelper.writableDatabase
    }

    fun close(){
        databaseHelper.close()

        if (database.isOpen)
            database.close()
    }

    fun queryAll(): Cursor = database.query(
                DATABASE_TABLE,
                null,
                null,
                null,
                null,
                null,
                "$_ID ASC"
        )

    fun queryById(id: String): Cursor = database.query(
            DATABASE_TABLE,
            null,
            "$_ID = ?",
            arrayOf(id),
            null,
            null,
            null,
            null
    )

    fun update(id: String, values: ContentValues?): Int = database.update(DATABASE_TABLE, values, "${BaseColumns._ID} = ?", arrayOf(id))

    fun insert(values: ContentValues?): Long = database.insert(DATABASE_TABLE, null, values)

    fun deleteByUsername(usernamed: String): Int = database.delete(DATABASE_TABLE, "$USERNAME = '$usernamed'", null)

    fun check(usernamed: String): Boolean {
        val selectId = "SELECT * FROM $DATABASE_TABLE WHERE $USERNAME = ?"
        val cursor = database.rawQuery(selectId, arrayOf(usernamed))
        var check = false

        if (cursor.moveToFirst()) {
            check = true
            var i = 0
            while (cursor.moveToNext()) {
                i++
            }
            Log.d(Constraints.TAG, String.format("%d records found", i))
        }
        cursor.close()
        return check
    }

}