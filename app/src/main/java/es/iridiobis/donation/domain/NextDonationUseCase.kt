package es.iridiobis.donation.domain

import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable


class NextDonationUseCase constructor(val donationRepository: DonationRepository) {

    companion object {
        val MILLIS_PER_YEAR = 31536000000L
        val MILLIS_PER_TWO_MONTHS = 5184000000L
        val MILLIS_PER_DAY = 86400000L
    }

    val nextRelay: BehaviorRelay<Long> = BehaviorRelay.create()

    init {
        donationRepository.retrieveLastYearDonations()
                .subscribe {
                    lastDonations ->
                    if (lastDonations.size > 3)
                        nextRelay.accept(Math.max(lastDonations[3] + MILLIS_PER_TWO_MONTHS, lastDonations[0] + MILLIS_PER_YEAR))
                    else if (lastDonations.isEmpty())
                        nextRelay.accept(removeExtraMillis(System.currentTimeMillis()))
                    else
                        nextRelay.accept(Math.max(lastDonations.last() + MILLIS_PER_TWO_MONTHS, removeExtraMillis(System.currentTimeMillis())))
                }
    }

    fun nextDonation(): Observable<Long> {
        return nextRelay
    }

    private fun removeExtraMillis(millis: Long) = millis - millis % MILLIS_PER_DAY

}