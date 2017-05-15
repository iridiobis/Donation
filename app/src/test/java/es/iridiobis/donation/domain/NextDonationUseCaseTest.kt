package es.iridiobis.donation.domain

import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`


class NextDonationUseCaseTest {

    @Test
    fun next_fourDonations_lastOlderThanTwoMonths() {
        val repo = mock(DonationRepository::class.java)
        val now = System.currentTimeMillis() - System.currentTimeMillis() % NextDonationUseCase.MILLIS_PER_DAY
        val donations = listOf(
                now - NextDonationUseCase.MILLIS_PER_TWO_MONTHS * 5,
                now - NextDonationUseCase.MILLIS_PER_TWO_MONTHS * 4,
                now - NextDonationUseCase.MILLIS_PER_TWO_MONTHS * 3,
                now - NextDonationUseCase.MILLIS_PER_TWO_MONTHS * 2)
        `when`(repo.retrieveLastYearDonations()).thenReturn(Observable.just(donations))
        val useCase = NextDonationUseCase(repo)

        val testObserver : TestObserver<Long> = TestObserver()
        useCase.nextDonation().subscribe(testObserver)

        testObserver.assertNoErrors()
        testObserver.assertValue(now - NextDonationUseCase.MILLIS_PER_TWO_MONTHS * 5 + NextDonationUseCase.MILLIS_PER_YEAR)
    }

    @Test
    fun next_fourDonations_lastNewerThanTwoMonths() {
        val repo = mock(DonationRepository::class.java)
        val now = System.currentTimeMillis() - System.currentTimeMillis() % NextDonationUseCase.MILLIS_PER_DAY
        val donations = listOf(
                now - NextDonationUseCase.MILLIS_PER_YEAR + NextDonationUseCase.MILLIS_PER_DAY,//a year ago minus a day
                now - NextDonationUseCase.MILLIS_PER_TWO_MONTHS * 4,
                now - NextDonationUseCase.MILLIS_PER_TWO_MONTHS * 3,
                now - NextDonationUseCase.MILLIS_PER_TWO_MONTHS / 2)//a month ago
        `when`(repo.retrieveLastYearDonations()).thenReturn(Observable.just(donations))
        val useCase = NextDonationUseCase(repo)

        val testObserver : TestObserver<Long> = TestObserver()
        useCase.nextDonation().subscribe(testObserver)

        testObserver.assertNoErrors()
        testObserver.assertValue(now + NextDonationUseCase.MILLIS_PER_TWO_MONTHS / 2)
    }

    @Test
    fun next_lessThanFourDonations_lastNewerThanTwoMonths() {
        val repo = mock(DonationRepository::class.java)
        val now = System.currentTimeMillis() - System.currentTimeMillis() % NextDonationUseCase.MILLIS_PER_DAY
        val donations = listOf(
                now - NextDonationUseCase.MILLIS_PER_TWO_MONTHS * 4,
                now - NextDonationUseCase.MILLIS_PER_TWO_MONTHS * 3,
                now - NextDonationUseCase.MILLIS_PER_TWO_MONTHS / 2)//a month ago
        `when`(repo.retrieveLastYearDonations()).thenReturn(Observable.just(donations))
        val useCase = NextDonationUseCase(repo)

        val testObserver : TestObserver<Long> = TestObserver()
        useCase.nextDonation().subscribe(testObserver)

        testObserver.assertNoErrors()
        testObserver.assertValue(now + NextDonationUseCase.MILLIS_PER_TWO_MONTHS / 2)
    }


    @Test
    fun next_lessThanFourDonations_lastNewerOlderThanTwoMonths() {
        val repo = mock(DonationRepository::class.java)
        val now = System.currentTimeMillis() - System.currentTimeMillis() % NextDonationUseCase.MILLIS_PER_DAY
        val donations = listOf(
                now - NextDonationUseCase.MILLIS_PER_TWO_MONTHS * 4,
                now - NextDonationUseCase.MILLIS_PER_TWO_MONTHS * 3,
                now - NextDonationUseCase.MILLIS_PER_TWO_MONTHS * 2)
        `when`(repo.retrieveLastYearDonations()).thenReturn(Observable.just(donations))
        val useCase = NextDonationUseCase(repo)

        val testObserver : TestObserver<Long> = TestObserver()
        useCase.nextDonation().subscribe(testObserver)

        testObserver.assertNoErrors()
        testObserver.assertValue(now)
    }

    @Test
    fun next_noDonation() {
        val repo = mock(DonationRepository::class.java)
        val now = System.currentTimeMillis() - System.currentTimeMillis() % NextDonationUseCase.MILLIS_PER_DAY
        val donations = ArrayList<Long>()
        `when`(repo.retrieveLastYearDonations()).thenReturn(Observable.just(donations))
        val useCase = NextDonationUseCase(repo)

        val testObserver : TestObserver<Long> = TestObserver()
        useCase.nextDonation().subscribe(testObserver)

        testObserver.assertNoErrors()
        testObserver.assertValue(now)
    }

}