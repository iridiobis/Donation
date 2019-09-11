package es.iridiobis.donation.presentation

import androidx.lifecycle.*
import es.iridiobis.donation.domain.*
import kotlinx.coroutines.launch
import javax.inject.Inject


class MainViewModel @Inject constructor(
        nextDonationUseCase: NextDonationUseCase,
        private val verifyDonationUseCase: VerifyDonationUseCase,
        private val addDonationUseCase: AddDonationUseCase)  : ViewModel() {
    val nextDonation : LiveData<Donation> = nextDonationUseCase.nextDonation

    fun verify(date : Long) : LiveData<DonationResult> = verifyDonationUseCase.verify(date)

    fun add(date : Long) : LiveData<DonationResult> = liveData {
        addDonationUseCase.suspendedAdd(Donation(date))
    }

}