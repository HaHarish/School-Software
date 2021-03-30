package com.newletseduvate.utils.rx_component_observables

import android.widget.Button
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject


object RxButtonObservable {
    fun fromView(button: Button): Observable<Boolean> {
        val subject = PublishSubject.create<Boolean>()
        button.setOnClickListener {
            subject.onNext(true)
        }
        return subject
    }
}