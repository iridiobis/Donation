package es.iridiobis.donation.data

import androidx.lifecycle.LiveData
import com.google.common.truth.Truth
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.*
import es.iridiobis.donation.domain.Donation
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito


class DonationStorageTest {

    lateinit var donationStorage : DonationStorage
    lateinit var donationDao : DonationDao

    @Before
    fun setUp() {
        donationDao = Mockito.mock(DonationDao::class.java)
        donationStorage = DonationStorage(donationDao)
    }

    @Test
    fun loadDonationSince_default() {
        val date = 1L
        val expectedResult = Mockito.mock(LiveData::class.java) as LiveData<List<Donation>>
        Mockito.`when`(donationDao.loadDonationsSince(date)).thenReturn(expectedResult)

        val result = donationStorage.loadDonationsSince(date)

        Truth.assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun `loadDonationInRange should return the donations in the range`() {
        runBlocking {
            val date = 1L
            val range = 2L
            val expectedResult = emptyList<Donation>()
            whenever(donationDao.loadDonationsInRangeSync(date, range)).doReturn(expectedResult)

            val result = donationStorage.loadDonationsInRange(date, range)

            assertThat(result).isEqualTo(expectedResult)
        }
    }

    @Test
    fun `addDonation should insert the donation`() {
        runBlocking {
            val donation = Donation(1L)

            donationStorage.add(donation)

            verify(donationDao).insertDonation(donation)
        }
    }
}