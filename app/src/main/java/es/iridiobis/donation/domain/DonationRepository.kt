package es.iridiobis.donation.domain

import androidx.lifecycle.LiveData
import io.reactivex.Completable

interface DonationRepository {

    /**
     * Provide the donations since the given date
     */
    fun loadDonationsSince(date: Long): LiveData<List<Donation>>

    /**
     * Provide the donations in the given range.
     */
    fun loadDonationsInRange(date: Long, range: Long): LiveData<List<Donation>>

    fun loadDonationsInRangeSync(date: Long, range: Long): List<Donation>

    suspend fun add(donation: Donation)

}