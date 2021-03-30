package com.newletseduvate.utils

data class Resource<out T>(val status: Status,
                           val data: T?,
                           val message: String?) {
    companion object {
        fun <T> success(data: T?): Resource<T> {
            return Resource(Status.Success, data, null)
        }

        fun <T> error(message: String): Resource<T> {
            return Resource(Status.Error,null, message)
        }

        fun <T> loading(): Resource<T> {
            return Resource(Status.Loading,null,null)
        }
    }
}

enum class Status {
    Loading,
    Success,
    Error
}

