package ru.itdo.tv

import android.app.Application
import ru.itdo.tv.data.AppContainer

class ItdoTvApp : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer()
    }
}
