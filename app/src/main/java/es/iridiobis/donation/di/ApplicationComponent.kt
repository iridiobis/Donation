package es.iridiobis.donation.di

import android.app.Application
import android.content.Context
import dagger.BindsInstance
import dagger.Component
import es.iridiobis.donation.DonationApp
import es.iridiobis.donation.data.DataModule
import es.iridiobis.donation.presentation.MainActivity
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(DataModule::class))
interface ApplicationComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance fun context(context: Context): Builder
        fun dataModule(dataModule: DataModule) : Builder
        fun build(): ApplicationComponent
    }

    fun inject(githubApp: DonationApp)
    fun inject(mainActivity: MainActivity)
}