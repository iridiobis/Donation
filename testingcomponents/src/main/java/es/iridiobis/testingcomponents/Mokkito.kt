package es.iridiobis.testingcomponents

import org.mockito.Mockito

fun <T> any(): T {
    Mockito.any<T>()
    return null as T
}