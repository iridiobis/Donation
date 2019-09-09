package es.iridiobis.donation.data

import androidx.room.Room
import android.content.Context
import dagger.Module
import dagger.Provides
import es.iridiobis.donation.di.JavaViewModelModule
import es.iridiobis.donation.di.ViewModelModule
import es.iridiobis.donation.domain.DonationRepository
import javax.inject.Singleton

@Module(includes = arrayOf(JavaViewModelModule::class, ViewModelModule::class)) class DataModule {
    @Singleton @Provides
    fun provideDonationDao(context: Context) : DonationDao {
        val dataBase = Room.databaseBuilder(context, DonationDatabase::class.java, "Donation DDBB").build()
        return dataBase.donationDao()
    }

    @Singleton @Provides
    fun provideDonationRepository(proxy: DonationStorage) : DonationRepository = proxy
}

