package com.noser.dojo


import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path


/**
 * Created by fabian.maechler on 06.04.2017.
 */
interface RestAPI {
    @GET("users/{user}/repos")
    fun listRepos(@Path("user") user: String): Observable<List<Repo>>
}

data class Repo(val name: String, val id: Int)
