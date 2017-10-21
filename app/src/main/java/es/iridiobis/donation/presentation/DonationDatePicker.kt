package es.iridiobis.donation.presentation

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import es.iridiobis.donation.R
import java.util.*


class DonationDatePicker : DialogFragment(), DatePicker.OnDateChangedListener {

    companion object Initializer {
        private val DATE = "DonationDatePicker.DATE"
        fun init(listener: DonationDateListener, date: Long = System.currentTimeMillis()): DonationDatePicker {
            val bundle = Bundle()
            bundle.putLong(DATE, date)
            val fragment = DonationDatePicker()
            fragment.arguments = bundle
            fragment.listener = listener
            return fragment
        }
    }

    lateinit var listener: DonationDateListener
    var donationDate = System.currentTimeMillis()

    @BindView(R.id.ddp_date_picker) lateinit var datePicker: DatePicker
    @BindView(R.id.ddp_error_message) lateinit var errorView: TextView

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val root = activity.layoutInflater.inflate(R.layout.date_picker_dialog, null, false)
        ButterKnife.bind(this, root)
        val calendar = extractCalendar()
        datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), this)
        return AlertDialog.Builder(activity, R.style.DatePickerDialogStyle)
                .setView(root)
                .setPositiveButton(R.string.ok) { _, _ -> listener.onDonation(donationDate) }
                .setNegativeButton(R.string.cancel, null)
                .create()
    }

    override fun onDateChanged(view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        donationDate = convertDateToLong(year, monthOfYear, dayOfMonth)
        listener.onDateChanged(donationDate)
    }

    fun showInvalideDate(invalid: Boolean) {
        errorView.visibility = if (invalid) View.VISIBLE else View.INVISIBLE
    }

    private fun extractCalendar(): Calendar {
        val calendar = GregorianCalendar()
        calendar.timeInMillis = arguments.getLong(DATE)
        return calendar
    }

    private fun convertDateToLong(year: Int, monthOfYear: Int, dayOfMonth: Int): Long {
        return GregorianCalendar(year, monthOfYear, dayOfMonth).timeInMillis
    }

    interface DonationDateListener {
        fun onDateChanged(date: Long)
        fun onDonation(date: Long)
    }

}