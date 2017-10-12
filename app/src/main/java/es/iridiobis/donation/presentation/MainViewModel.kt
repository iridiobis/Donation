package es.iridiobis.donation.presentation

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import es.iridiobis.donation.domain.Donation
import es.iridiobis.donation.domain.NextDonationUseCase
import javax.inject.Inject


class MainViewModel @Inject constructor(nextDonationUseCase: NextDonationUseCase)  : ViewModel() {
    val nextDonation : LiveData<Donation> = nextDonationUseCase.nextDonation
}