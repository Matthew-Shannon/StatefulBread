package com.matthew.statefulbread.repo

interface IDataService {
    fun getUser(): List<String>
}

class DataService(val prefsService: IPrefsService): IDataService {
    override fun getUser(): List<String> = listOf(
        "Name: "+prefsService.getString("name"),
        "Email: "+prefsService.getString("email"),
        "Zip Code: "+prefsService.getString("zipCode"),
        "Password: "+prefsService.getString("password"),
    )
}
