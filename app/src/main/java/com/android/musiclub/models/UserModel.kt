package com.android.musiclub.models

data class UserModel(
    var userName: String,
    var userUid: String,
    var userImageUri: String?,
    var pushToken: String?
){
    constructor() : this("", "", "", "")
}
