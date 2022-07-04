package com.teasers.picsum.data


import androidx.paging.PagingSource
import com.teasers.picsum.data.model.PicSum
import com.teasers.picsum.data.remote.api.PicSumService
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody

import org.junit.Assert.*
import org.junit.Test
import retrofit2.Response
import java.io.IOException
import java.net.UnknownHostException

class PicSumPagingSourceTest {
    private val apiService: PicSumService = mockk<PicSumService>()
    private val picSumPagingSource = PicSumPagingSource(apiService)

    private val picsum = PicSum(
        "1016",
        "Philippe Wuyts",
        3844,
        2563,
        "https://unsplash.com/photos/_h7aBovKia4",
        "https://picsum.photos/id/1016/3844/2563"
    )
    private val picsumResponse = listOf(picsum)

    @Test
    fun `picsums paging source append - success`() = runBlocking {

        // Given that the service responds with success
        val apiResult = Response.success(picsumResponse)

        coEvery { apiService.getPhotos(any(), any()) } returns (apiResult)


        val expectedResult = PagingSource.LoadResult.Page(
            data = picsumResponse,
            prevKey = 0,
            nextKey = 2
        )
        assertEquals(
            expectedResult, picSumPagingSource.load(
                PagingSource.LoadParams.Append(
                    key = 1,
                    loadSize = 1,
                    placeholdersEnabled = false
                )
            )
        )
    }

    @Test
    fun `reviews paging source prepend - success`() = runBlocking {
        val apiResult = Response.success(picsumResponse)

        coEvery { apiService.getPhotos(any(), any()) } returns (apiResult)
        val expectedResult = PagingSource.LoadResult.Page(
            data = picsumResponse,
            prevKey = null,
            nextKey = 1
        )
        assertEquals(
            expectedResult, picSumPagingSource.load(
                PagingSource.LoadParams.Prepend(
                    key = 0,
                    loadSize = 20,
                    placeholdersEnabled = false
                )
            )
        )
    }

    @Test
    fun `reviews paging source refresh - success`() = runBlocking {
        val apiResult = Response.success(picsumResponse)

        coEvery { apiService.getPhotos(any(), any()) } returns (apiResult)

        val expectedResult = PagingSource.LoadResult.Page(
            data = picsumResponse,
            prevKey = null,
            nextKey = 1
        )
        assertEquals(
            expectedResult, picSumPagingSource.load(
                PagingSource.LoadParams.Refresh(
                    key = 0,
                    loadSize = 20,
                    placeholdersEnabled = false
                )
            )
        )
    }


    @Test
    fun `reviews paging source load - failure - http error`() = runBlocking {
        val errorResponseBody = "Error".toResponseBody("".toMediaTypeOrNull())
        val apiResult = Response.error<List<PicSum>>(
            500,
            errorResponseBody
        )
        coEvery { apiService.getPhotos(any(), any()) } returns (apiResult)
        val result = picSumPagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = 0,
                loadSize = 1,
                placeholdersEnabled = false
            )
        )
        assertTrue(result is PagingSource.LoadResult.Error)
    }

    @Test
    fun `reviews paging source load - failure - received null`() = runBlocking {
        coEvery { apiService.getPhotos(any(), any()) } .throws(UnknownHostException())
        val result = picSumPagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = 0,
                loadSize = 1,
                placeholdersEnabled = false
            )
        )


        assertTrue(result is PagingSource.LoadResult.Error)
    }

}