package es.iridiobis.donation.domain

import android.arch.lifecycle.LiveData

interface DonationRepository {
    fun loadDonationsSince(date: Long): LiveData<List<Donation>>
}