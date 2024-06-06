package com.bangkit.sebatik.data.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.bangkit.sebatik.data.UserPreferences
import com.bangkit.sebatik.data.response.DatasItem
import com.bangkit.sebatik.data.retrofit.ApiService
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class ProductPagingSource(
    private val apiService: ApiService,
    private val pref: UserPreferences
): PagingSource<Int, DatasItem>() {

    override fun getRefreshKey(state: PagingState<Int, DatasItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DatasItem> {
        return try {
            val token =  runBlocking {
                pref.getToken().first()
            }
            val position = params.key ?: INITIAL_PAGE_INDEX
            val responseData = apiService.getBatik("Bearer $token")
            LoadResult.Page(
                data = responseData.payload.datas,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position -1,
                nextKey = if (responseData.payload.datas.isNullOrEmpty()) null else position +1
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }


    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}