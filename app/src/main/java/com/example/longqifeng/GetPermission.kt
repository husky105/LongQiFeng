package com.example.longqifeng

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

object GetPermission {

    public fun checkPermission(ctx: Context, permissions: List<String>) {
        val REQUEST = 1
        var permission = 0
        for (item in permissions) {
            permission += ActivityCompat.checkSelfPermission(ctx, item)
        }
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                (ctx as Activity),
                permissions.toTypedArray(),
                REQUEST
            )
        }
    }
}