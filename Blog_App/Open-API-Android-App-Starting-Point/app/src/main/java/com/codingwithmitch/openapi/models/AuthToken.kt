package com.codingwithmitch.openapi.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

const val AUTH_TOKEN_BUNDLE_KEY = "com.codingwithmitch.openapi.models.AuthToken"
@Parcelize
@Entity(
    tableName = "auth_token",

    /*
    Making relation between foreign key
     */
    foreignKeys = [
        ForeignKey(
            // This is the table that has relation
            entity =  AccountProperties::class,

            //The foreign key
            parentColumns = ["pk"],

            //Table to store the foreign key
            childColumns = ["account_pk"],


            /*
            What happen when the relation table deleted in this case the AccounProperties::class
            CASCADE -> WHEN PARENT DELTED THAN THIS TABLE ALSO DELETED
             */
            onDelete = CASCADE
        )
    ]
)
data class AuthToken(

    @PrimaryKey
    @ColumnInfo(name = "account_pk")
    var account_pk:Int? = -1,


    @SerializedName("token")
    @Expose
    @ColumnInfo(name="token")
    var token: String? = null
) : Parcelable