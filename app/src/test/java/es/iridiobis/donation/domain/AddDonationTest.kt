package es.iridiobis.donation.domain

import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.Mockito.never

class AddDonationTest {

    @Test
    fun `add should success when no donations in range`() {
        val donation = Donation(System.currentTimeMillis() - System.currentTimeMillis() % MILLIS_PER_DAY)
        val donations = ArrayList<Donation>()

        val repo = mock<DonationRepository> {
            onBlocking { loadDonationsInRange(anyLong(), anyLong()) } doReturn donations
        }

        val sut = AddDonation(repo)

        runBlocking {
            val result = sut(donation)

            assertThat(result.successful).isTrue()
            assertThat(result.donations).isEmpty()
            verify(repo).add(donation)
        }

    }

    @Test
    fun `add should fail when there are donations in range`() {
        val now = System.currentTimeMillis() - System.currentTimeMillis() % MILLIS_PER_DAY
        val donation = Donation(now)
        val donations = listOf(
                Donation(now - MILLIS_PER_TWO_MONTHS / 2),
                Donation(now + MILLIS_PER_TWO_MONTHS / 2))
        val repo = mock<DonationRepository> {
            onBlocking { loadDonationsInRange(anyLong(), anyLong()) } doReturn donations
        }

        val sut = AddDonation(repo)

        runBlocking {
            val result = sut(donation)

            assertThat(result.successful).isFalse()
            assertThat(result.donations).isEqualTo(donations)
            verify(repo, never()).add(donation)
        }

    }

}