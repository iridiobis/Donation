package es.iridiobis.donation.domain

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import com.google.common.truth.Truth
import es.iridiobis.testingcomponents.asLiveData
import es.iridiobis.testingcomponents.retrieveValue
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*
import org.junit.Rule




class NextDonationUseCaseTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun next_fourDonations_aYearFromFirstGreaterThanTwoMonthsFromLast() {
        val repo = mock(DonationRepository::class.java)
        val now = System.currentTimeMillis() - System.currentTimeMillis() % MILLIS_PER_DAY
        val donations = listOf(
                Donation(now - MILLIS_PER_TWO_MONTHS * 5),
                Donation(now - MILLIS_PER_TWO_MONTHS * 4),
                Donation(now - MILLIS_PER_TWO_MONTHS * 3),
                Donation(now - MILLIS_PER_TWO_MONTHS * 2))
        `when`(repo.loadDonationsSince(ArgumentMatchers.anyLong())).thenReturn(donations.asLiveData())
        val useCase = NextDonationUseCase(repo)

        val next = useCase.nextDonation.retrieveValue()

        Truth.assertThat(now - MILLIS_PER_TWO_MONTHS * 5 + MILLIS_PER_YEAR).isEqualTo(next.date)
    }

    @Test
    fun next_fourDonations_aYearFromFirstSmallerThanTwoMonthsFromLast() {
        val repo = mock(DonationRepository::class.java)
        val now = System.currentTimeMillis() - System.currentTimeMillis() % MILLIS_PER_DAY
        val donations = listOf(
                Donation(now - MILLIS_PER_YEAR + MILLIS_PER_DAY),//a year ago minus a day
                Donation(now - MILLIS_PER_TWO_MONTHS * 4),
                Donation(now - MILLIS_PER_TWO_MONTHS * 3),
                Donation(now - MILLIS_PER_TWO_MONTHS / 2))//a month ago
        `when`(repo.loadDonationsSince(ArgumentMatchers.anyLong())).thenReturn(donations.asLiveData())
        val useCase = NextDonationUseCase(repo)

        val next = useCase.nextDonation.retrieveValue()

        Truth.assertThat(now + MILLIS_PER_TWO_MONTHS / 2).isEqualTo(next.date)
    }

    @Test
    fun next_lessThanFourDonations_lastNewerThanTwoMonths() {
        val repo = mock(DonationRepository::class.java)
        val now = System.currentTimeMillis() - System.currentTimeMillis() % MILLIS_PER_DAY
        val donations = listOf(
                Donation(now - MILLIS_PER_TWO_MONTHS * 4),
                Donation(now - MILLIS_PER_TWO_MONTHS * 3),
                Donation(now - MILLIS_PER_TWO_MONTHS / 2))//a month ago
        `when`(repo.loadDonationsSince(ArgumentMatchers.anyLong())).thenReturn(donations.asLiveData())
        val useCase = NextDonationUseCase(repo)

        val next = useCase.nextDonation.retrieveValue()

        Truth.assertThat(now + MILLIS_PER_TWO_MONTHS / 2).isEqualTo(next.date)
    }


    @Test
    fun next_lessThanFourDonations_lastOlderThanTwoMonths() {
        val repo = mock(DonationRepository::class.java)
        val now = System.currentTimeMillis() - System.currentTimeMillis() % MILLIS_PER_DAY
        val donations = listOf(
                Donation(now - MILLIS_PER_TWO_MONTHS * 4),
                Donation(now - MILLIS_PER_TWO_MONTHS * 3),
                Donation(now - MILLIS_PER_TWO_MONTHS * 2))
        `when`(repo.loadDonationsSince(ArgumentMatchers.anyLong())).thenReturn(donations.asLiveData())
        val useCase = NextDonationUseCase(repo)

        val next = useCase.nextDonation.retrieveValue()

        Truth.assertThat(now).isEqualTo(next.date)
    }

    @Test
    fun next_noDonation() {
        val repo = mock(DonationRepository::class.java)
        val now = System.currentTimeMillis() - System.currentTimeMillis() % MILLIS_PER_DAY
        val donations = ArrayList<Donation>()
        `when`(repo.loadDonationsSince(ArgumentMatchers.anyLong())).thenReturn(donations.asLiveData())
        val useCase = NextDonationUseCase(repo)

        val next = useCase.nextDonation.retrieveValue()

        Truth.assertThat(now).isEqualTo(next.date)
    }

}