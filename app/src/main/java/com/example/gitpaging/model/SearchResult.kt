package com.example.gitpaging.model

import java.lang.Exception

sealed class SearchResult{
    data class Success(val data: List<Repository>) : SearchResult()
    data class Error(val error: Exception) : SearchResult()
}
