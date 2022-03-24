package com.bbd.gifsrepository.data

data class LocalException(
    val throwable: Throwable,
    val code: Int = -1
) : Exception()