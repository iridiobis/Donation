package es.iridiobis.donation.domain

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class Donation(@PrimaryKey val date: Long)