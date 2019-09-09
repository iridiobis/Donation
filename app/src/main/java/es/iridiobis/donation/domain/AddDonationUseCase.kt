package es.iridiobis.donation.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Logic to add a new donation. The result is a {@see DonationResult}, which can be successful
 * or not. If it is unsuccessful, it will provide a list of donations that make the donation invalid.
 */
class AddDonationUseCase @Inject constructor(val donationRepository: DonationRepository) {

    /**
     * Adds the provided donation to the donations repository.
     */
    fun add(donation : Donation) : LiveData<DonationResult> {
        return Transformations.map(
                donationRepository.loadDonationsInRange(donation.date, MILLIS_PER_TWO_MONTHS),
                { donationsInRange -> addDonation(donation, donationsInRange) })
    }

    private fun addDonation(donation : Donation, donationsInRange : List<Donation>) : DonationResult {
        if (donationsInRange.isEmpty()) {
            donationRepository.addDonation(donation)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe()
        }
        return DonationResult(donationsInRange.isEmpty(), donationsInRange)
    }

}