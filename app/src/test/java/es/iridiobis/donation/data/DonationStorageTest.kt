package es.iridiobis.donation.data

import android.arch.lifecycle.LiveData
import com.google.common.truth.Truth
import es.iridiobis.donation.domain.Donation
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
    fun loadDonationInRange_default() {
        val date = 1L
        val range = 2L
        val expectedResult = Mockito.mock(LiveData::class.java) as LiveData<List<Donation>>
        Mockito.`when`(donationDao.loadDonationsInRange(date, range)).thenReturn(expectedResult)

        val result = donationStorage.loadDonationsInRange(date, range)

        Truth.assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun addDonation_default() {
        val donation = Donation(1L)

        donationStorage.addDonation(donation)

        Mockito.verify(donationDao).insertDonation(donation)
    }
}