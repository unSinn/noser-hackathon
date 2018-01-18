package com.noser.hackathon


import retrofit2.http.GET
import retrofit2.http.Path


/**
 * Created by fabian.maechler on 06.04.2017.
 */
interface GameServerAPI {

    @GET("hackathon/ninjas")
    fun getNinjas(): List<String>

    @GET("hackathon/ninjas/{name}")
    fun getNinja(@Path("name") name: String): Ninja
}

data class Ninja(val name: String, val preferedWeapon: String)



