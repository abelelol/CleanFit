package com.example.cleanfit.di

import javax.inject.Qualifier


// really cool way to store and have multiple clients.

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SerplyClient