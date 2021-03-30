package com.newletseduvate.utils.extensions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

fun <T, K, R> LiveData<T>.combineWith(
    liveData: LiveData<K>,
    block: (T?, K?) -> R
): LiveData<R> {
    val result = MediatorLiveData<R>()
    result.addSource(this) {
        result.value = block(this.value, liveData.value)
    }
    result.addSource(liveData) {
        result.value = block(this.value, liveData.value)
    }
    return result
}

fun <T, L,M,N, R> LiveData<T>.combineWith(
    liveData1: LiveData<L>,
    liveData2: LiveData<M>,
    liveData3: LiveData<N>,
    block: (T?, L?, M?, N?) -> R
): LiveData<R> {
    val result = MediatorLiveData<R>()
    result.addSource(this) {
        result.value = block(this.value, liveData1.value, liveData2.value, liveData3.value)
    }

    result.addSource(liveData1) {
        result.value = block(this.value, liveData1.value, liveData2.value, liveData3.value)
    }

    result.addSource(liveData2) {
        result.value = block(this.value, liveData1.value, liveData2.value, liveData3.value)
    }

    result.addSource(liveData3) {
        result.value = block(this.value, liveData1.value, liveData2.value, liveData3.value)
    }
    return result
}