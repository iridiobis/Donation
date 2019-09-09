package es.iridiobis.donation.di;

import androidx.lifecycle.ViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
import es.iridiobis.donation.presentation.MainViewModel;

//TODO right now intomap is not working with kotlin. Use java until the issue is solved
@Module
public abstract class JavaViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel.class)
    abstract ViewModel provideMainViewModel(MainViewModel viewModel);

}
