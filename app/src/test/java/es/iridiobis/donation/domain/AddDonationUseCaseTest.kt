package es.iridiobis.donation.domain

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import es.iridiobis.testingcomponents.any
import es.iridiobis.testingcomponents.asLiveData
import es.iridiobis.testingcomponents.retrieveValue
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.Mockito
import org.mockito.Mockito.never
import org.mockito.Mockito.times

class AddDonationUseCaseTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun add_noDonationsInRange_success() {
        val repo = Mockito.mock(DonationRepository::class.java)
        val donation = Donation(System.currentTimeMillis() - System.currentTimeMillis() % MILLIS_PER_DAY)
        val donations = ArrayList<Donation>()
        Mockito.`when`(repo.loadDonationsInRange(anyLong(), anyLong())).thenReturn(donations.asLiveData())
        val useCase = AddDonationUseCase(repo)

        val result = useCase.add(donation).retrieveValue()

        Truth.assertThat(result.successful).isTrue()
        Truth.assertThat(result.donations).isEmpty()
        Mockito.verify(repo, times(1)).addDonation(donation)
    }

    @Test
    fun add_donationsInRange_success() {
        val repo = Mockito.mock(DonationRepository::class.java)
        val now = System.currentTimeMillis() - System.currentTimeMillis() % MILLIS_PER_DAY
        val donations = listOf(
                Donation(now - MILLIS_PER_TWO_MONTHS/2),
                Donation(now + MILLIS_PER_TWO_MONTHS/2))
        Mockito.`when`(repo.loadDonationsInRange(anyLong(), anyLong())).thenReturn(donations.asLiveData())
        val useCase = AddDonationUseCase(repo)

        val result = useCase.add(Donation(now)).retrieveValue()

        Truth.assertThat(result.successful).isFalse()
        Truth.assertThat(result.donations).isEqualTo(donations)
        Mockito.verify(repo, never()).addDonation(any())

    }

}