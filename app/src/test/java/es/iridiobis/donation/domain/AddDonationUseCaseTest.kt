package es.iridiobis.donation.domain

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.*
import es.iridiobis.testingcomponents.*
import es.iridiobis.testingcomponents.any
import io.reactivex.Completable
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.*
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.Mockito
import org.mockito.Mockito.never
import org.mockito.Mockito.times

class AddDonationUseCaseTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setComputationSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setNewThreadSchedulerHandler { Schedulers.trampoline() }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
    }

    @Test
    fun `add should success when no donations in range`() {
        val donation = Donation(System.currentTimeMillis() - System.currentTimeMillis() % MILLIS_PER_DAY)
        val donations = ArrayList<Donation>()

        val repo = mock<DonationRepository> {
            on { loadDonationsInRangeSync(anyLong(), anyLong()) } doReturn donations
        }

        val useCase = AddDonationUseCase(repo)

        runBlocking {
            val result = useCase.suspendedAdd(donation)

            assertThat(result.successful).isTrue()
            assertThat(result.donations).isEmpty()
            verify(repo).addDonationSync(donation)
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
            on { loadDonationsInRangeSync(anyLong(), anyLong()) } doReturn donations
        }

        val useCase = AddDonationUseCase(repo)

        runBlocking {
            val result = useCase.suspendedAdd(donation)

            assertThat(result.successful).isFalse()
            assertThat(result.donations).isEqualTo(donations)
            verify(repo, never()).addDonationSync(donation)
        }

    }

    @Test
    fun add_noDonationsInRange_success() {
        val repo = Mockito.mock(DonationRepository::class.java)
        val donation = Donation(System.currentTimeMillis() - System.currentTimeMillis() % MILLIS_PER_DAY)
        val donations = ArrayList<Donation>()
        Mockito.`when`(repo.loadDonationsInRange(anyLong(), anyLong())).thenReturn(donations.asLiveData())
        Mockito.`when`(repo.addDonation(donation)).thenReturn(Completable.complete())
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
                Donation(now - MILLIS_PER_TWO_MONTHS / 2),
                Donation(now + MILLIS_PER_TWO_MONTHS / 2))
        Mockito.`when`(repo.loadDonationsInRange(anyLong(), anyLong())).thenReturn(donations.asLiveData())
        val useCase = AddDonationUseCase(repo)

        val result = useCase.add(Donation(now)).retrieveValue()

        Truth.assertThat(result.successful).isFalse()
        Truth.assertThat(result.donations).isEqualTo(donations)
        Mockito.verify(repo, never()).addDonation(any())

    }

}