package com.bangkit.sebatik.data.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.bangkit.sebatik.data.response.ProductResponseItem
import com.bangkit.sebatik.data.retrofit.ApiService

class ProductPagingSource(
    private val apiService: ApiService
): PagingSource<Int, ProductResponseItem>() {

    override fun getRefreshKey(state: PagingState<Int, ProductResponseItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ProductResponseItem> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val responseData = apiService.getAllProducts(position, params.loadSize)
            LoadResult.Page(
                data = responseData,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position -1,
                nextKey = if (responseData.isNullOrEmpty()) null else position +1
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }


    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}