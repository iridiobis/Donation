package es.iridiobis.donation.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import javax.inject.Inject

class VerifyDonation @Inject constructor(private val donationRepository: DonationRepository) {

    operator fun invoke(date : Long) : LiveData<DonationResult> {
        return Transformations.map(
                donationRepository.loadDonationsInRange(date, MILLIS_PER_TWO_MONTHS),
                { donationsInRange -> DonationResult(donationsInRange.isEmpty(), donationsInRange) }
        )
    }

}