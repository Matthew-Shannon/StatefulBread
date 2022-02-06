package com.matthew.statefulbread.repo

import io.reactivex.rxjava3.core.Observable

interface IDataService {
    fun getUser(): Observable<List<String>>
}

class DataService(val prefsService: IPrefsService): IDataService {
    override fun getUser(): Observable<List<String>> = Observable.just(
        listOf(
            "Name: "+prefsService.getName(),
            "Email: "+prefsService.getEmail(),
            "Zip Code: "+prefsService.getZipCode(),
            "Password: "+prefsService.getPassword(),
        )
    )


}
