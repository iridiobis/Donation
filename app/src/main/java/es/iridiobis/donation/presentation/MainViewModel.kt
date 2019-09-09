package es.iridiobis.donation.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import es.iridiobis.donation.domain.*
import javax.inject.Inject


class MainViewModel @Inject constructor(
        nextDonationUseCase: NextDonationUseCase,
        private val verifyDonationUseCase: VerifyDonationUseCase,
        private val addDonationUseCase: AddDonationUseCase)  : ViewModel() {
    val nextDonation : LiveData<Donation> = nextDonationUseCase.nextDonation

    fun verify(date : Long) : LiveData<DonationResult> = verifyDonationUseCase.verify(date)

    fun add(date : Long) : LiveData<DonationResult> = addDonationUseCase.add(Donation(date))
}