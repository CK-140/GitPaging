package com.example.gitpaging.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
interface RetrofitInterface {


    interface GithubService {
        @GET("search/repositories?sort=stars")
        suspend fun searchRepos(
            @Query("q") query: String,
            @Query("page") page: Int,
            @Query("per_page") itemsPerPage: Int
        ): SearchResponse

        companion object {
            private const val BASE_URL = "https://api.github.com/"

            fun create(): GithubService {

                val client = OkHttpClient.Builder()
                    .build()
                return Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(GithubService::class.java)
            }
        }
    }

}