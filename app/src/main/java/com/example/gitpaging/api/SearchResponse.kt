package com.example.gitpaging.api

import com.example.gitpaging.model.Repository
import com.google.gson.annotations.SerializedName

data class SearchResponse(
    @SerializedName("total_count") val total: Int = 0,
    @SerializedName("items") val items: List<Repository> = emptyList(),
    val nextPage: Int? = null
)
