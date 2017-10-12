package es.iridiobis.donation.domain

/**
 * This class represents the result of a new donation. It can be successful or not. If not, it
 * will provide the list of donation entries that make the new entry invalid.
 *
 */
class NewDonationResult(val successful : Boolean, val donations : List<Donation>)