package es.iridiobis.donation.presentation

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.widget.Toolbar
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.*
import es.iridiobis.donation.DonationApp
import es.iridiobis.donation.R
import es.iridiobis.donation.domain.Donation
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, DonationDatePicker.DonationDateListener {
    override fun onDateChanged(date: Long) {
        viewModel.verify(date).observe(this, Observer { result -> donationDatePicker?.showInvalideDate(!(result!!.successful)) })
    }

    override fun onDonation(date: Long) {
        viewModel.add(date).observe(this, Observer { result -> result?.let { if(!result.successful) Toast.makeText(this, R.string.main_insertion_error, Toast.LENGTH_SHORT).show() }  })
    }

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel : MainViewModel
    var donationDatePicker : DonationDatePicker? = null

    override fun onBackPressed() {
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id = item.itemId

        if (id == R.id.nav_donations) {
            // Handle the donations action
        } else if (id == R.id.nav_centers) {
            //Handle the centers action
        } else if (id == R.id.nav_manage) {
            //Handle the configuration action
        }

        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as DonationApp).getComponent().inject(this)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        //setSupportActionBar(toolbar)

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            donationDatePicker = DonationDatePicker.init(this)
            donationDatePicker!!.show(fragmentManager, "Date picker")
        }

        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.setDrawerListener(toggle)
        toggle.syncState()

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        attachToViewModel()
    }

    private fun attachToViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)
        viewModel.nextDonation.observe(this, Observer { nextDonation -> updateNextDonation(nextDonation!!) })
    }

    private fun updateNextDonation(donation: Donation) {
        val nextDonationView = findViewById<TextView>(R.id.main_next_donation_date)
        nextDonationView.text = SimpleDateFormat.getDateInstance().format(Date(donation.date))
    }
}
