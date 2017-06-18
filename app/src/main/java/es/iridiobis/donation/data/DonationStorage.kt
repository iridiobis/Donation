package es.iridiobis.donation.data

import android.arch.lifecycle.LiveData
import es.iridiobis.donation.domain.Donation
import es.iridiobis.donation.domain.DonationRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DonationStorage @Inject constructor(val donationDao: DonationDao) : DonationRepository {

    override fun loadDonationsSince(date: Long): LiveData<List<Donation>> = donationDao.loadDonationsSince(date)

    override fun loadDonationsInRange(date: Long, range: Long): LiveData<List<Donation>> = donationDao.loadDonationsInRange(date, range)

    override fun addDonation(donation: Donation) {
        donationDao.insertDonation(donation)
    }

}