package com.codingwithmitch.openapi.persistence

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.codingwithmitch.openapi.models.AccountProperties


@Dao
interface AccountPropertiesDao {


    //When there is conflic then replace
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAndReplace(accountProperties: AccountProperties) : Long // Long return witch row that inserted/Repclaced


    // When there is conflict just ignore it
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnore(accountProperties: AccountProperties) : Long

    @Query("SELECT * FROM acccount_properties WHERE pk = :pk")
    suspend fun searchByPk(pk:Int) : AccountProperties

    @Query("SELECT * FROM acccount_properties WHERE email = :email")
    suspend fun searchByEmail(email:String) : AccountProperties?

    @Query("UPDATE acccount_properties SET email = :email, username = :username WHERE pk = :pk")
    suspend fun updateAccountProperties(pk : Int , email : String , username : String)


}