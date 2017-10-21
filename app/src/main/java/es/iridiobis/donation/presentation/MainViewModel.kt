package es.iridiobis.donation.presentation

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import es.iridiobis.donation.domain.Donation
import es.iridiobis.donation.domain.DonationResult
import es.iridiobis.donation.domain.NextDonationUseCase
import es.iridiobis.donation.domain.VerifyDonationUseCase
import javax.inject.Inject


class MainViewModel @Inject constructor(nextDonationUseCase: NextDonationUseCase, private val verifyDonationUseCase: VerifyDonationUseCase)  : ViewModel() {
    val nextDonation : LiveData<Donation> = nextDonationUseCase.nextDonation

    fun verify(date : Long) : LiveData<DonationResult> = verifyDonationUseCase.verify(date)
}