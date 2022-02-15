package com.machiav3lli.discus.client

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.preference.PreferenceManager
import java.io.File

var Context.result: Int
    get() = PreferenceManager.getDefaultSharedPreferences(this).getInt("result", 1)
    set(value) {
        PreferenceManager.getDefaultSharedPreferences(this).edit()
            .putInt("result", value).apply()
    }

val Context.projectsDirectory: File
    get() = File(
        getExternalFilesDir(null)?.parentFile?.parentFile?.parentFile?.parentFile?.canonicalPath,
        "Discus"
    )

val Context.hasStoragePermissions: Boolean
    get() = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.R ->
            Environment.isExternalStorageManager()
        else ->
            checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_GRANTED
    }

fun Activity.getStoragePermission() {
    when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
            val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
            intent.data = Uri.parse("package:$packageName")
            startActivity(intent)
        }
        else -> {
            requireWriteStoragePermission()
            requireReadStoragePermission()
        }
    }
}

private fun Activity.requireReadStoragePermission() {
    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) !=
        PackageManager.PERMISSION_GRANTED
    )
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), READ_PERMISSION
        )
}

private fun Activity.requireWriteStoragePermission() {
    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
        PackageManager.PERMISSION_GRANTED
    )
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), WRITE_PERMISSION
        )
}