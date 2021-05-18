package com.example.gitpaging.data

import android.util.Log
import com.example.gitpaging.api.GithubService
import com.example.gitpaging.api.IN_QUALIFIER
import com.example.gitpaging.model.Repository
import com.example.gitpaging.model.SearchResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import retrofit2.HttpException
import java.io.IOException

private const val GITHUB_STARTING_PAGE_INDEX = 1

class GithubRepository(private val service: GithubService) {


    private val inMemoryCache = mutableListOf<Repository>()


    private val searchResults = MutableSharedFlow<SearchResult>(replay = 1)


    private var lastRequestedPage = GITHUB_STARTING_PAGE_INDEX


    private var isRequestInProgress = false

    suspend fun getSearchResultStream(query: String): Flow<SearchResult> {
        Log.d("GithubRepository", "New query: $query")
        lastRequestedPage = 1
        inMemoryCache.clear()
        requestAndSaveData(query)

        return searchResults
    }

    suspend fun requestMore(query: String) {
        if (isRequestInProgress) return
        val successful = requestAndSaveData(query)
        if (successful) {
            lastRequestedPage++
        }
    }

    suspend fun retry(query: String) {
        if (isRequestInProgress) return
        requestAndSaveData(query)
    }

    private suspend fun requestAndSaveData(query: String): Boolean {
        isRequestInProgress = true
        var successful = false

        val apiQuery = query + IN_QUALIFIER
        try {
            val response = service.searchRepos(apiQuery, lastRequestedPage, NETWORK_PAGE_SIZE)
            Log.d("GithubRepository", "response $response")
            val repos = response.items ?: emptyList()
            inMemoryCache.addAll(repos)
            val reposByName = reposByName(query)
            searchResults.emit(SearchResult.Success(reposByName))
            successful = true
        } catch (exception: IOException) {
            searchResults.emit(SearchResult.Error(exception))
        } catch (exception: HttpException) {
            searchResults.emit(SearchResult.Error(exception))
        }
        isRequestInProgress = false
        return successful
    }

    private fun reposByName(query: String): List<Repository> {
        return inMemoryCache.filter {
            it.name.contains(query, true) ||
                    (it.description != null && it.description.contains(query, true))
        }.sortedWith(compareByDescending<Repository> { it.stars }.thenBy { it.name })
    }

    companion object {
        private const val NETWORK_PAGE_SIZE = 50
    }
}