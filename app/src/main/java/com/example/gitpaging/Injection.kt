package com.example.gitpaging

import androidx.lifecycle.ViewModelProvider
import com.example.gitpaging.api.GithubService
import com.example.gitpaging.data.GithubRepository
import com.example.gitpaging.ui.ViewModelFactory

object Injection {


    private fun provideGithubRepository(): GithubRepository {
        return GithubRepository(GithubService.create())
    }


    fun provideViewModelFactory(): ViewModelProvider.Factory {
        return ViewModelFactory(provideGithubRepository())
    }
}