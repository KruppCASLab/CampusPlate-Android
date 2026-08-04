package edu.cwru.caslab.campusplate

import android.app.Application
import edu.cwru.caslab.campusplate.di.AppContainer

class CampusPlateApp : Application() {

    lateinit var appContainer: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        appContainer = AppContainer(this)
    }
}
