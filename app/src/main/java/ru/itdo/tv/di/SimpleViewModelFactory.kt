package ru.itdo.tv.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/** Минимальная фабрика для ViewModel с конструктор-аргументами, без Hilt. */
class SimpleViewModelFactory(private val creator: () -> ViewModel) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = creator() as T
}
