package es.iridiobis.donation.data

import androidx.lifecycle.LiveData
import es.iridiobis.donation.domain.Donation
import es.iridiobis.donation.domain.DonationRepository
import io.reactivex.Completable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DonationStorage @Inject constructor(val donationDao: DonationDao) : DonationRepository {

    override fun loadDonationsSince(date: Long): LiveData<List<Donation>> = donationDao.loadDonationsSince(date)

    override fun loadDonationsInRange(date: Long, range: Long): LiveData<List<Donation>> = donationDao.loadDonationsInRange(date, range)

    override fun addDonation(donation: Donation): Completable {
        return Completable.fromAction { donationDao.insertDonation(donation) }
    }

}