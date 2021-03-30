package com.newletseduvate.utils.extensions

import android.content.SharedPreferences
import android.util.Log
import com.newletseduvate.utils.constants.ApiConstants.BEARER
import com.newletseduvate.utils.constants.PrefKeys

fun SharedPreferences.setLoginResponse(value: String?){
    val editor = edit()
    editor.putString(PrefKeys.LOGIN_RESPONSE, value)
    editor.apply()
}
fun SharedPreferences.getLoginResponse(): String?{
    return getString(PrefKeys.LOGIN_RESPONSE, null)
}


fun SharedPreferences.setUserId(value: Int){
    val editor = edit()
    editor.putInt(PrefKeys.USER_ID, value)
    editor.apply()
}
fun SharedPreferences.getUserId(): Int{
    return getInt(PrefKeys.USER_ID, -1)
}


fun SharedPreferences.setUserFirstName(value: String?){
    val editor = edit()
    editor.putString(PrefKeys.USER_FIRST_NAME, value)
    editor.apply()
}
fun SharedPreferences.getUserFirstName(): String?{
    return getString(PrefKeys.USER_FIRST_NAME, null)
}


fun SharedPreferences.setUserLastName(value: String?){
    val editor = edit()
    editor.putString(PrefKeys.USER_LAST_NAME, value)
    editor.apply()
}
fun SharedPreferences.getUserLastName(): String?{
    return getString(PrefKeys.USER_LAST_NAME, null)
}


fun SharedPreferences.setToken(value: String?){
    val editor = edit()
    editor.putString(PrefKeys.TOKEN, "$BEARER$value")
    editor.apply()
}
fun SharedPreferences.getToken(): String?{
    return getString(PrefKeys.TOKEN, null)
}


fun SharedPreferences.setIsSuperUser(value:Boolean){
    val editor = edit()
    editor.putBoolean(PrefKeys.IS_SUPER_USER, value)
    editor.apply()
}
fun SharedPreferences.getIsSuperUser(): Boolean{
    return getBoolean(PrefKeys.IS_SUPER_USER, false)
}


fun SharedPreferences.setPassword(value: String?){
    val editor = edit()
    editor.putString(PrefKeys.PASSWORD, value)
    editor.apply()
}
fun SharedPreferences.getPassword(): String?{
    return getString(PrefKeys.PASSWORD, null)
}


fun SharedPreferences.setIsUserLoggedIn(value: Boolean){
    val editor = edit()
    editor.putBoolean(PrefKeys.IS_USER_LOGGED_IN, value)
    editor.apply()
}
fun SharedPreferences.getIsUserLoggedIn(): Boolean{
    return getBoolean(PrefKeys.IS_USER_LOGGED_IN, false)
}

fun SharedPreferences.setErpUserId(value: Int){
    val editor = edit()
    editor.putInt(PrefKeys.ERP_USER_ID, value)
    editor.apply()
}
fun SharedPreferences.getErpUserId(): Int{
    return getInt(PrefKeys.ERP_USER_ID, 0)
}

fun SharedPreferences.setRoleId(value: Int){
    val editor = edit()
    editor.putInt(PrefKeys.ROLE_ID, value)
    editor.apply()
}
fun SharedPreferences.getRoleId(): Int{
    return getInt(PrefKeys.ROLE_ID, 0)
}

fun SharedPreferences.setBranchName(value: String){
    val editor = edit()
    editor.putString(PrefKeys.BRANCH_NAME, value)
    editor.apply()
    Log.d("TAGY","BranchSet: "+value)
}
fun SharedPreferences.getBranchName(): String{
    Log.d("TAGY","BranchGet: "+getString(PrefKeys.BRANCH_NAME, ""))
    return getString(PrefKeys.BRANCH_NAME, "")!!
}

fun SharedPreferences.setBaseUrl(value: String){
    val editor = edit()
    editor.putString(PrefKeys.BASE_URL_CONSTANT, value)
    editor.apply()
}
fun SharedPreferences.getBaseUrl(): String{
    return getString(PrefKeys.BASE_URL_CONSTANT, "").toString()
}

fun SharedPreferences.setUsernameLogin(value: String){
    val editor = edit()
    editor.putString(PrefKeys.USER_NAME_LOGIN, value)
    editor.apply()
}
fun SharedPreferences.getUsernameLogin(): String{
    return getString(PrefKeys.USER_NAME_LOGIN, "").toString()
}

fun SharedPreferences.setPasswordLogin(value: String){
    val editor = edit()
    editor.putString(PrefKeys.USER_PASSWORD_LOGIN, value)
    editor.apply()
}
fun SharedPreferences.getPasswordLogin(): String{
    return getString(PrefKeys.USER_PASSWORD_LOGIN, "").toString()
}

fun SharedPreferences.setRememberMe(value: Int){
    val editor = edit()
    editor.putInt(PrefKeys.REMEMBER_ME, value)
    editor.apply()
}
fun SharedPreferences.getRememberMe(): Int{
    return getInt(PrefKeys.REMEMBER_ME, 0)
}

fun SharedPreferences.setUserIdNew(value: String){
    val editor = edit()
    editor.putString(PrefKeys.USER_ID_NEW, value)
    editor.apply()
}
fun SharedPreferences.getUserIdNew(): String{
    return getString(PrefKeys.USER_ID_NEW, "0").toString()
}