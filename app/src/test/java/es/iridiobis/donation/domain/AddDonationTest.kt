package es.iridiobis.donation.domain

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.*
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.Mockito.never

class AddDonationTest {

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

        val sut = AddDonation(repo)

        runBlocking {
            val result = sut(donation)

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

        val sut = AddDonation(repo)

        runBlocking {
            val result = sut(donation)

            assertThat(result.successful).isFalse()
            assertThat(result.donations).isEqualTo(donations)
            verify(repo, never()).addDonationSync(donation)
        }

    }

}