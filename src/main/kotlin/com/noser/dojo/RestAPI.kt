package com.noser.dojo


import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path


/**
 * Created by fabian.maechler on 06.04.2017.
 */
interface RestAPI {

    @GET("dojo/ninjas")
    fun getNinjas(): Observable<List<String>>

    @GET("dojo/ninjas/{name}")
    fun getNinja(@Path("name") name: String): Observable<Ninja>
}

data class Ninja(val name: String, val preferedWeapon: String)


