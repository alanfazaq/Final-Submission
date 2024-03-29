package com.dicoding.final_submission_bfaa.Helper

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.dicoding.final_submission_bfaa.Database.DBContract.FavoriteColumns.Companion.COLUMNS_USERNAME
import com.dicoding.final_submission_bfaa.Database.DBContract.FavoriteColumns.Companion.TABLE_NAME
import com.dicoding.final_submission_bfaa.Database.DBHelper

class UserHelper(context: Context) {

    companion object {
        private const val TABLE_DATABASE = TABLE_NAME
        private var INSTANCE: UserHelper? = null
        private lateinit var databaseHelper: DBHelper
        private lateinit var sqLiteDatabase: SQLiteDatabase

        fun getInstance(context: Context): UserHelper = INSTANCE ?: synchronized(this) {
            INSTANCE ?: UserHelper(context)
        }
    }

    init {
        databaseHelper = DBHelper(context)
    }

    @Throws
    fun open() {
        sqLiteDatabase = databaseHelper.writableDatabase
    }

    fun close() {
        databaseHelper.close()
        if (sqLiteDatabase.isOpen) {
            sqLiteDatabase.close()
        }
    }

    fun queryAll(): Cursor {
        return sqLiteDatabase.query(
            TABLE_DATABASE,
            null,
            null,
            null,
            null,
            null,
            "$COLUMNS_USERNAME ASC"
        )
    }

    fun queryById(id: String): Cursor {
        return sqLiteDatabase.query(
            TABLE_DATABASE,
            null,
            "$COLUMNS_USERNAME = ?",
            arrayOf(id),
            null,
            null,
            null,
            null
        )
    }

    fun insertDB(values: ContentValues?): Long {
        return sqLiteDatabase.insert(TABLE_DATABASE, null, values)
    }

    fun updateDB(id: String, values: ContentValues?): Int {
        return sqLiteDatabase.update(TABLE_DATABASE, values, "$COLUMNS_USERNAME = ?", arrayOf(id))
    }

    fun deleteDB(id: String): Int {
        return sqLiteDatabase.delete(TABLE_DATABASE, "$COLUMNS_USERNAME = '$id'", null)
    }
}