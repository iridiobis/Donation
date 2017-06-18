package es.iridiobis.donation.domain

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import javax.inject.Inject


class AddDonationUseCase @Inject constructor(val donationRepository: DonationRepository) {

    fun add(donation : Donation) : LiveData<NewDonationResult> {
        return Transformations.map(
                donationRepository.loadDonationsInRange(donation.date, MILLIS_PER_TWO_MONTHS),
                { donationsInRange -> addDonation(donation, donationsInRange) })
    }

    private fun addDonation(donation : Donation, donationsInRange : List<Donation>) : NewDonationResult {
        if (donationsInRange.isEmpty()) {
            donationRepository.addDonation(donation)
        }
        return NewDonationResult(donationsInRange.isEmpty(), donationsInRange)
    }

}