package dev.anvith.binanceninja.features.ui.core

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import androidx.activity.ComponentActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import dev.anvith.binanceninja.di.AppScope
import java.lang.ref.WeakReference
import me.tatarka.inject.annotations.Inject

@Inject
@AppScope
actual class PermissionHandler(
    private val context: Context,
) {

    private var permissionRequestId = 1
    private var permissionRequests =
        mutableMapOf<Int, WeakReference<Pair<() -> Unit, () -> Unit>>>()

    private lateinit var activity: WeakReference<ComponentActivity>
    fun updateActivity(activity: ComponentActivity) {
        this.activity = WeakReference(activity)
    }

    fun onRequestPermissionsResult(
        requestCode: Int,
        grantResults: IntArray
    ) {
        val request = permissionRequests[requestCode]?.get()
        if (request != null) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                request.first()
            } else {
                request.second()
            }
        }
        permissionRequests.remove(requestCode)
    }

    actual fun requestPermission(
        permission: PermissionType,
        onGranted: () -> Unit,
        onDenied: () -> Unit
    ) {
        val requestId = permissionRequestId++
        permissionRequests[requestId] = WeakReference(Pair(onGranted, onDenied))
        val activityContext = activity.get()
        val androidPermission = permission.asAndroidPermission()
        if (androidPermission != null) {

            if (activityContext == null) {
                permissionRequests.remove(requestId)
                return
            }
            if (ContextCompat.checkSelfPermission(
                    context,
                    androidPermission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // Permission is not granted
                // Request the permission
                ActivityCompat.requestPermissions(
                    activityContext,
                    arrayOf(androidPermission),
                    requestId
                );
            } else {
                onGranted()
            }
        } else {
            onGranted()
        }

    }

    private fun PermissionType.asAndroidPermission(): String? = when (this) {
        PermissionType.NOTIFICATION -> if (VERSION.SDK_INT >= VERSION_CODES.TIRAMISU) {
            Manifest.permission.POST_NOTIFICATIONS
        } else {
            null
        }
    }

    actual fun hasPermission(permission: PermissionType, onPermissionResult: (Boolean) -> Unit) {
        val androidPermission = permission.asAndroidPermission()
        return if (androidPermission != null) {
            onPermissionResult(
                ContextCompat.checkSelfPermission(
                    context,
                    androidPermission
                ) == PackageManager.PERMISSION_GRANTED
            )
        } else {
            onPermissionResult(true)
        }
    }

}