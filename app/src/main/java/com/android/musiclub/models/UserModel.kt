package com.android.musiclub.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel(
    var userName: String = "",
    var userUid: String = "",
    var userImageUri: String? = "",
    var pushToken: String? = ""
) : Parcelable
