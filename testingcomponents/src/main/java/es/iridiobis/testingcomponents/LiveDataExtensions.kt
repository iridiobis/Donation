package es.iridiobis.testingcomponents

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.Observer

/**
 * Retrieves the NON NULL value of a live data. Use only on live data instances that are gonna
 * emmit a non null value.
 */
@Throws(InterruptedException::class)
fun <T> LiveData<T>.retrieveValue(): T {
    val data = arrayOfNulls<Any>(1)
    val latch = java.util.concurrent.CountDownLatch(1)
    val observer = object : Observer<T> {
        override fun onChanged(o: T?) {
            data[0] = o
            latch.countDown()
            this@retrieveValue.removeObserver(this)
        }
    }
    this.observeForever(observer)
    latch.await(2, java.util.concurrent.TimeUnit.SECONDS)

    @Suppress("UNCHECKED_CAST")
    //Set on onChanged, it is always T
    return data[0] as T
}

fun <T> List<T>.asLiveData() : LiveData<List<T>> {
    return object : MediatorLiveData<List<T>>(){
        init {
            value = this@asLiveData
        }
    }
}