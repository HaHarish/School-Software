package com.newletseduvate.utils.rx_component_observables

import androidx.appcompat.widget.SearchView
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

object RxSearchObservable {
    fun fromView(searchView: SearchView): Observable<String> {
        val subject = PublishSubject.create<String>()
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if(query.isNotEmpty())
                    subject.onNext(query)
                return true
            }

            override fun onQueryTextChange(query: String): Boolean {
                if(query.isNotEmpty())
                    subject.onNext(query)
                return true
            }
        })
        return subject
    }
}