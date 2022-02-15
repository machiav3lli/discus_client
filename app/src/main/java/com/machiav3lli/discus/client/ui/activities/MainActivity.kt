package com.machiav3lli.discus.client.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.machiav3lli.discus.client.KiezHubClient
import com.machiav3lli.discus.client.R
import com.machiav3lli.discus.client.databinding.MainActivityBinding
import com.machiav3lli.discus.client.getStoragePermission
import com.machiav3lli.discus.client.hasStoragePermissions
import com.machiav3lli.discus.client.ui.fragments.MainFragment

class MainActivity : AppCompatActivity() {
    lateinit var binding: MainActivityBinding

    val db
        get() = (application as KiezHubClient).db

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
    }

    override fun onResume() {
        super.onResume()
        if (!hasStoragePermissions) getStoragePermission()
    }
}
