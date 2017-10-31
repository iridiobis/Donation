package es.iridiobis.donation.data

import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.Database
import es.iridiobis.donation.domain.Donation

@Database(entities = arrayOf(Donation::class), version = 1)
abstract class DonationDatabase : RoomDatabase() {
    abstract fun donationDao(): DonationDao
}