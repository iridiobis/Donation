package es.iridiobis.donation.presentation

import android.arch.lifecycle.LifecycleActivity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import es.iridiobis.donation.R
import android.arch.lifecycle.ViewModelProviders
import android.test.ApplicationTestCase
import android.widget.TextView
import es.iridiobis.donation.DonationApp
import es.iridiobis.donation.domain.Donation
import es.iridiobis.temporizador.core.di.ComponentProvider
import javax.inject.Inject


class MainActivity : LifecycleActivity(), NavigationView.OnNavigationItemSelectedListener {

    @Inject lateinit var viewModelFactory : ViewModelProvider.Factory

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
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
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
        val model = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)
        model.nextDonation.observe(this, Observer { nextDonation -> updateNextDonation(nextDonation!!) })
    }

    private fun updateNextDonation(donation : Donation) {
        val nextDonationView = findViewById<TextView>(R.id.main_next_donation_date)
        nextDonationView.text = donation.date.toString()
    }
}
