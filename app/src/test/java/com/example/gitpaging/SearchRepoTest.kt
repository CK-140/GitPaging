package com.example.gitpaging

import com.example.gitpaging.api.GithubService
import com.example.gitpaging.data.GithubRepository
import com.example.gitpaging.ui.SearchRepositoriesViewModel
import org.junit.Before
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class SearchRepoTest {
    lateinit var  searchRepoModel: SearchRepositoriesViewModel

    @Mock
    lateinit var searchRepo: GithubRepository

    @Before
    fun setupTest(){
        MockitoAnnotations.initMocks(this)
        searchRepoModel = SearchRepositoriesViewModel(GithubRepository(GithubService.create()))
    }
}