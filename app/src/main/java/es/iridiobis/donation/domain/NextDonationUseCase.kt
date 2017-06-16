package es.iridiobis.donation.domain

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import javax.inject.Inject

class NextDonationUseCase @Inject constructor(donationRepository: DonationRepository) {

    val nextDonation: LiveData<Donation>

    init {
        nextDonation = Transformations.map(
                donationRepository.loadDonationsSince(System.currentTimeMillis() - MILLIS_PER_YEAR),
                { lastDonations -> calculateNextDonation(lastDonations) })
    }

    fun nextDonation(): LiveData<Donation> = nextDonation

    private fun removeExtraMillis(millis: Long) = millis - millis % MILLIS_PER_DAY

    private fun calculateNextDonation(lastDonations: List<Donation>): Donation {
        val nextDate = if (lastDonations.size > 3)
            Math.max(lastDonations[3].date + MILLIS_PER_TWO_MONTHS, lastDonations[0].date + MILLIS_PER_YEAR)
        else if (lastDonations.isEmpty())
            removeExtraMillis(System.currentTimeMillis())
        else
            Math.max(lastDonations.last().date + MILLIS_PER_TWO_MONTHS, removeExtraMillis(System.currentTimeMillis()))
        return Donation(nextDate)
    }

}