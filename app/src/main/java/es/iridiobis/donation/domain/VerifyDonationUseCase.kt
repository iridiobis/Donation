package es.iridiobis.donation.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import javax.inject.Inject

/**
 * Logic to check if a date is valid for a donation. The result is a {@see DonationResult}, which can be successful
 * or not. If it is unsuccessful, it will provide a list of donations that make the donation invalid.
 */
class VerifyDonationUseCase @Inject constructor(private val donationRepository: DonationRepository) {

    /**
     * Verify the provided date
     */
    fun verify(date : Long) : LiveData<DonationResult> {
        return Transformations.map(
                donationRepository.loadDonationsInRange(date, MILLIS_PER_TWO_MONTHS),
                { donationsInRange -> DonationResult(donationsInRange.isEmpty(), donationsInRange) }
        )
    }

}