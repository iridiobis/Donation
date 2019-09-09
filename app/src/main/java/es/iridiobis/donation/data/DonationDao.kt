package es.iridiobis.donation.data

import androidx.lifecycle.LiveData
import androidx.room.*
import es.iridiobis.donation.domain.Donation

@Dao
interface DonationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDonation(donation: Donation)

    @Delete
    fun deleteDonation(donation: Donation)

    @Query("SELECT * FROM donation")
    fun loadDonations(): LiveData<List<Donation>>

    @Query("SELECT * FROM donation WHERE date > :date")
    fun loadDonationsSince(date: Long): LiveData<List<Donation>>

    @Query("SELECT * FROM donation WHERE date > :date - :range AND date < :date + :range")
    fun loadDonationsInRange(date: Long, range: Long): LiveData<List<Donation>>

    @Query("SELECT * FROM donation WHERE date > :date - :range AND date < :date + :range")
    fun loadDonationsInRangeSync(date: Long, range: Long): List<Donation>

    @Query("SELECT * FROM donation ORDER BY date DESC LIMIT 1")
    fun loadLastDonation() : LiveData<Donation>

}