package es.iridiobis.donation.data

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth
import es.iridiobis.donation.domain.Donation
import es.iridiobis.testingcomponents.retrieveValue
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.runner.RunWith
import java.io.IOException


@RunWith(AndroidJUnit4::class)
class DonationDaoTest {

    companion object {
        val MILLIS_PER_MONTH = 2592000000L
    }

    private lateinit var donationDao: DonationDao
    private lateinit var dataBase: DonationDatabase

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        dataBase = Room.inMemoryDatabaseBuilder(context, DonationDatabase::class.java).build()
        donationDao = dataBase.donationDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        dataBase.close()
    }

    @Test
    fun deleteAfterInsert() {
        runBlocking {

            val donation = Donation(0)
            donationDao.insertDonation(donation)
            val donations = donationDao.loadDonations().retrieveValue()
            Truth.assertThat(donations.size).isEqualTo(1)
            donationDao.deleteDonation(donation)
            Truth.assertThat(donationDao.loadDonations().retrieveValue()).isEmpty()
        }
    }

    @Test
    fun loadDonations_twoYears() {
        setUpDonationsInTwoYears()
        val donations = donationDao.loadDonations().retrieveValue()
        Truth.assertThat(donations.size).isEqualTo(8)
    }

    @Test
    fun loadDonationsSince_lastYear() {
        val now = setUpDonationsInTwoYears()
        val donations = donationDao.loadDonationsSince(now - 12 * MILLIS_PER_MONTH).retrieveValue()
        Truth.assertThat(donations.size).isEqualTo(4)
    }

    @Test
    fun loadDonationsInRange_none() {
        val now = setUpDonationsInTwoYears()
        val donations = donationDao.loadDonationsInRange(now - 125 * MILLIS_PER_MONTH / 10, 2 * MILLIS_PER_MONTH).retrieveValue()
        Truth.assertThat(donations).isEmpty()
    }

    @Test
    fun loadDonationsInRange_nonEmpty() {
        val now = setUpDonationsInTwoYears()
        val donations = donationDao.loadDonationsInRange(now - 6 * MILLIS_PER_MONTH, 2 * MILLIS_PER_MONTH).retrieveValue()
        Truth.assertThat(donations).isNotEmpty()
    }

    @Test
    fun loadLastDonation_noDonations() {
        val donation = donationDao.loadLastDonation().retrieveValue()
        Truth.assertThat(donation).isNull()
    }

    @Test
    fun loadLastDonation_twoYearDonations() {
        val now = setUpDonationsInTwoYears()
        val donation = donationDao.loadLastDonation().retrieveValue()
        Truth.assertThat(donation.date).isEqualTo(now - 1 * MILLIS_PER_MONTH)
    }

    private fun setUpDonationsInTwoYears(): Long {
        val now = System.currentTimeMillis()
        runBlocking {

            donationDao.insertDonation(Donation(now - 24 * MILLIS_PER_MONTH))
            donationDao.insertDonation(Donation(now - 20 * MILLIS_PER_MONTH))
            donationDao.insertDonation(Donation(now - 17 * MILLIS_PER_MONTH))
            donationDao.insertDonation(Donation(now - 15 * MILLIS_PER_MONTH))
            donationDao.insertDonation(Donation(now - 10 * MILLIS_PER_MONTH))
            donationDao.insertDonation(Donation(now - 7 * MILLIS_PER_MONTH))
            donationDao.insertDonation(Donation(now - 5 * MILLIS_PER_MONTH))
            donationDao.insertDonation(Donation(now - 1 * MILLIS_PER_MONTH))
        }
        return now
    }

}