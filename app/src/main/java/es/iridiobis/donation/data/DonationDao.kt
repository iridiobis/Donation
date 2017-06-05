package es.iridiobis.donation.data

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import es.iridiobis.donation.domain.Donation

@Dao
interface DonationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDonation(donation: Donation)

    @Delete
    fun deleteDonation(donation: Donation)

    @Query("SELECT * FROM donation")
    fun loadDonations(): LiveData<List<Donation>>

    @Query("SELECT * FROM donation WHERE date > :arg0")
    fun loadDonationsSince(date: Long): LiveData<List<Donation>>

    @Query("SELECT * FROM donation WHERE date > :arg0 - :arg1 AND date < :arg0 + :arg1")
    fun loadDonationsInRange(date: Long, range: Long): LiveData<List<Donation>>

}