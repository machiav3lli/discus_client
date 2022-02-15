package com.machiav3lli.discus.client

import android.app.Application
import com.machiav3lli.discus.client.data.Database

class KiezHubClient : Application() {
    lateinit var db: Database

    override fun onCreate() {
        super.onCreate()
        db = Database.getInstance(applicationContext)
    }
}
