package com.bbd.gifsrepository.data.repository

import com.bbd.gifsrepository.data.LocalException
import com.bbd.gifsrepository.data.consts.*
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

open class BaseRepository {

    protected fun <T> getFailedResponse(response: Response<T>): LocalException {
        return errorMapper(HttpException(response))
    }

    protected fun errorMapper(exception: Exception): LocalException {
        return when (exception) {
            is HttpException -> {
                LocalException(exception, exception.response()?.code() ?: UNKNOWN_HTTP_EXCEPTION)
            }
            is UnknownHostException -> {
                LocalException(exception, UNKNOWN_HOST_EXCEPTION)
            }
            is IOException -> {
                LocalException(exception, IO_EXCEPTION)
            }
            is TimeoutException -> {
                LocalException(exception, TIMEOUT_EXCEPTION)
            }
            is SocketTimeoutException -> {
                LocalException(exception, SOCKET_TIMEOUT_EXCEPTION)
            }
            else -> {
                LocalException(exception, UNKNOWN_EXCEPTION)
            }
        }
    }
}