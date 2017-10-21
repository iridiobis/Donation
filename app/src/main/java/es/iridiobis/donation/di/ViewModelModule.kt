package es.iridiobis.donation.di

import android.arch.lifecycle.ViewModelProvider

import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: JavaViewModelFactory): ViewModelProvider.Factory

}

