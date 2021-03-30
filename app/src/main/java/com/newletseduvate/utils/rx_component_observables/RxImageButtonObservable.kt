package com.newletseduvate.utils.rx_component_observables

import android.widget.ImageButton
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

object RxImageButtonObservable {
    fun fromView(button: ImageButton): Observable<Boolean> {
        val subject = PublishSubject.create<Boolean>()
        button.setOnClickListener {
            subject.onNext(true)
        }
        return subject
    }
}