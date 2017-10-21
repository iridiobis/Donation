package es.iridiobis.temporizador.core.di


interface ComponentProvider<out T> {
    fun getComponent() : T
}


