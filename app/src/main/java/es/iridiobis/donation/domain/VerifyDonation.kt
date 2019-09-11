package es.iridiobis.donation.domain

import javax.inject.Inject

class VerifyDonation @Inject constructor(private val donationRepository: DonationRepository) {

    suspend operator fun invoke(date : Long) : DonationResult {
        val inRange = donationRepository.loadDonationsInRange(date, MILLIS_PER_TWO_MONTHS)
        return DonationResult(inRange.isEmpty(), inRange)
    }

}