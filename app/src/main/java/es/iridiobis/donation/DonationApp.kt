package es.iridiobis.donation

import android.app.Application
import es.iridiobis.donation.data.DataModule
import es.iridiobis.donation.di.ApplicationComponent
import es.iridiobis.donation.di.DaggerApplicationComponent
import es.iridiobis.temporizador.core.di.ComponentProvider


class DonationApp : Application(), ComponentProvider<ApplicationComponent> {

    private lateinit var component : ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        component = DaggerApplicationComponent.builder()
                .dataModule(DataModule())
                .context(applicationContext)
                .build()
    }

    override fun getComponent(): ApplicationComponent = component

}