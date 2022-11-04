package com.example.noteskeeping.model

data class Document(
    val createTime: String,
    val fields: Fields,
    val name: String,
    val updateTime: String
)