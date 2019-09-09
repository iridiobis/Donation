package es.iridiobis.donation.di

import androidx.lifecycle.ViewModel

import dagger.MapKey
import kotlin.reflect.KClass

@MustBeDocumented
@Target(AnnotationTarget.FUNCTION)
@Retention
@MapKey
internal annotation class ViewModelKey(val value: KClass<out ViewModel>)