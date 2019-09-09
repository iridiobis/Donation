package es.iridiobis.donation.data

import androidx.room.RoomDatabase
import androidx.room.Database
import es.iridiobis.donation.domain.Donation

@Database(entities = arrayOf(Donation::class), version = 1)
abstract class DonationDatabase : RoomDatabase() {
    abstract fun donationDao(): DonationDao
}