package com.matthew.statefulbread.repo

interface IData {
    fun getUser(): List<String>
}

class Data(val prefs: IPrefs): IData {
    override fun getUser(): List<String> = listOf(
        "Name: "+prefs.getString("name"),
        "Email: "+prefs.getString("email"),
        "Zip Code: "+prefs.getString("zipCode"),
        "Password: "+prefs.getString("password"),
    )
}
