package es.iridiobis.donation.domain

import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.ArgumentMatchers.anyLong

class VerifyDonationTest {

    @Test
    fun `verify should success when no donations in range`() {
        runBlocking {
            val now = System.currentTimeMillis() - System.currentTimeMillis() % MILLIS_PER_DAY
            val donations = ArrayList<Donation>()

            val repo = mock<DonationRepository> {
                onBlocking { loadDonationsInRange(anyLong(), anyLong()) } doReturn donations
            }

            val sut = VerifyDonation(repo)

            val result = sut(now)

            assertThat(result.successful).isTrue()
            assertThat(result.donations).isEmpty()
        }

    }

    @Test
    fun `add should fail when there are donations in range`() {
        runBlocking {
            val now = System.currentTimeMillis() - System.currentTimeMillis() % MILLIS_PER_DAY
            val donation = Donation(now)
            val donations = listOf(
                    Donation(now - MILLIS_PER_TWO_MONTHS / 2),
                    Donation(now + MILLIS_PER_TWO_MONTHS / 2))
            val repo = mock<DonationRepository> {
                onBlocking { loadDonationsInRange(anyLong(), anyLong()) } doReturn donations
            }

            val sut = AddDonation(repo)

            val result = sut(donation)

            assertThat(result.successful).isFalse()
            assertThat(result.donations).isEqualTo(donations)
        }

    }

}