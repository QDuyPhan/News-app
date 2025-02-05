package com.example.newsapp.di.module

import com.example.newsapp.utils.DefaultDispatcher
import com.example.newsapp.utils.IODispatcher
import com.example.newsapp.utils.MainDispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
object CoroutinesDispatcherModule {
    @Provides
    @IODispatcher
    fun provideIODispatchers(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @MainDispatcher
    fun provideMainDispatchers(): CoroutineDispatcher = Dispatchers.Main

    @Provides
    @DefaultDispatcher
    fun provideDefaultDispatchers(): CoroutineDispatcher = Dispatchers.Default
}