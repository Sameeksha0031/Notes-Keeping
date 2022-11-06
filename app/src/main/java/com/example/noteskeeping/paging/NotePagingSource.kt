package com.example.noteskeeping.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.noteskeeping.model.Fields

class NotePagingSource(val notePagingSource: NotePagingSource) : PagingSource<String,Fields>() {
    override suspend fun load(params: LoadParams<String>): LoadResult<String, Fields> {
        TODO("Not yet implemented")
    }

}