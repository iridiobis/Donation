package es.iridiobis.donation.presentation

import androidx.lifecycle.*
import es.iridiobis.donation.domain.*
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject


class MainViewModel @Inject constructor(
        nextDonationUseCase: NextDonationUseCase,
        private val verifyDonation: VerifyDonation,
        private val addDonation: AddDonation)  : ViewModel() {

    val nextDonation : LiveData<Donation> = nextDonationUseCase.nextDonation

    fun verify(date : Long) : LiveData<DonationResult> = liveData(Dispatchers.IO) {
        verifyDonation(date)
    }

    fun add(date : Long) : LiveData<DonationResult> = liveData(Dispatchers.IO) {
        addDonation(Donation(date))
    }

}