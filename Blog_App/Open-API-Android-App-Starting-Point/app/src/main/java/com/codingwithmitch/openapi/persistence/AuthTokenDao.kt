package com.codingwithmitch.openapi.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.codingwithmitch.openapi.models.AuthToken


@Dao
interface AuthTokenDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(authToken: AuthToken) : Long // Return the row that got inserted

    @Query("UPDATE auth_token SET token = null WHERE account_pk = :pk")
    fun nullifyToken(pk:Int):Int
}