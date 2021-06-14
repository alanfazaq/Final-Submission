package com.dicoding.final_submission_bfaa.Helper

import android.database.Cursor
import com.dicoding.final_submission_bfaa.Database.DBContract
import com.dicoding.final_submission_bfaa.Model.User

object Mapping {
    fun mapCursorToArrayList(cursor: Cursor?): ArrayList<User> {
        val listFavorite = ArrayList<User>()
        cursor?.apply {
            while (moveToNext()) {
                val username = getString(getColumnIndexOrThrow(DBContract.FavoriteColumns.COLUMNS_USERNAME))
                val type = getString(getColumnIndexOrThrow(DBContract.FavoriteColumns.COLUMNS_TYPE))
                val avatar = getString(getColumnIndexOrThrow(DBContract.FavoriteColumns.COLUMNS_AVATAR))
                val location = getString(getColumnIndexOrThrow(DBContract.FavoriteColumns.COLUMNS_LOCATION))
                val company = getString(getColumnIndexOrThrow(DBContract.FavoriteColumns.COLUMNS_COMPANY))
                val repository = getString(getColumnIndexOrThrow(DBContract.FavoriteColumns.COLUMNS_REPOSITORY))
                listFavorite.add(User(username, type, avatar, location, company, repository))
            }
        }
        return listFavorite
    }
}