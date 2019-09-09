package es.iridiobis.donation.domain

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import es.iridiobis.testingcomponents.asLiveData
import es.iridiobis.testingcomponents.retrieveValue
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito

class VerifyDonationUseCaseTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun verify_noDonationsInRange_success() {
        val repo = Mockito.mock(DonationRepository::class.java)
        val donations = ArrayList<Donation>()
        Mockito.`when`(repo.loadDonationsInRange(ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong())).thenReturn(donations.asLiveData())
        val useCase = VerifyDonationUseCase(repo)

        val result = useCase.verify(System.currentTimeMillis()).retrieveValue()

        Truth.assertThat(result.successful).isTrue()
        Truth.assertThat(result.donations).isEmpty()
    }

    @Test
    fun verify_donationsInRange_invalid() {
        val repo = Mockito.mock(DonationRepository::class.java)
        val now = System.currentTimeMillis() - System.currentTimeMillis() % MILLIS_PER_DAY
        val donations = listOf(
                Donation(now - MILLIS_PER_TWO_MONTHS / 2),
                Donation(now + MILLIS_PER_TWO_MONTHS / 2))
        Mockito.`when`(repo.loadDonationsInRange(ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong())).thenReturn(donations.asLiveData())
        val useCase = VerifyDonationUseCase(repo)

        val result = useCase.verify(now).retrieveValue()

        Truth.assertThat(result.successful).isFalse()
        Truth.assertThat(result.donations).isEqualTo(donations)

    }

}