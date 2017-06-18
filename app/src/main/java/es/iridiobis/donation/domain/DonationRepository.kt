package es.iridiobis.donation.domain

import android.arch.lifecycle.LiveData

interface DonationRepository {

    fun loadDonationsSince(date: Long): LiveData<List<Donation>>

    fun loadDonationsInRange(date: Long, range: Long): LiveData<List<Donation>>

    fun addDonation(donation : Donation)

}