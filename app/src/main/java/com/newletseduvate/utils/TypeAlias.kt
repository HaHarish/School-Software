package com.newletseduvate.utils

typealias OnSuccess<R> = (R) -> Unit
typealias OnError<R> = (R) -> Unit

typealias OnClickedYes<R> = (R) -> Unit
typealias OnClickedNo<R> = (R) -> Unit

typealias OnClickAction<R> = (R) -> Unit