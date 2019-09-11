package es.iridiobis.donation.domain

import javax.inject.Inject

class AddDonation @Inject constructor(val donationRepository: DonationRepository) {

    suspend operator fun invoke(donation: Donation): DonationResult {
        val inRange = donationRepository.loadDonationsInRange(donation.date, MILLIS_PER_TWO_MONTHS)
        if (inRange.isEmpty()) {
            donationRepository.add(donation)
        }
        return DonationResult(inRange.isEmpty(), inRange)
    }

}