//package com.example.newsapp.di.module
//
//import androidx.lifecycle.ViewModelProvider
//import com.example.newsapp.di.viewmodel.ViewModelFactory
//import dagger.Module
//import dagger.Provides
//import dagger.hilt.InstallIn
//import dagger.hilt.components.SingletonComponent
//import javax.inject.Singleton
//
//@Module
//@InstallIn(SingletonComponent::class)
//object ViewModelFactoryModule {
//
//    @Provides
//    @Singleton
//    fun provideViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory {
//        return viewModelFactory
//    }
//}