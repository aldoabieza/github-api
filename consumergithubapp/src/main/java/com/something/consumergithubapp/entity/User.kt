package com.something.consumergithubapp.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
        var id: Int = 0,
        var username: String? = null,
        var url: String? = null,
        var photo: String? = null,
        var name: String? = null,
        var repository: String? = null,
        var followers: String? = null,
        var following: String? = null,
        var location: String? = null,
        var company: String? = null
) : Parcelable