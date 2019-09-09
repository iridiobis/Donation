package es.iridiobis.donation.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Donation(@PrimaryKey var date: Long)