package es.iridiobis.donation.domain

import io.reactivex.Observable

interface DonationRepository {
    fun retrieveLastYearDonations() : Observable<List<Long>>
}