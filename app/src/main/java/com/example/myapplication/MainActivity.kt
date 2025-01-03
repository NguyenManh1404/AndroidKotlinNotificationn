package com.example.myapplication

import android.content.pm.*
import android.os.*
import android.util.*
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.*
import androidx.core.content.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.*
import java.io.IOException


//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//    }
//
//    fun requestPermission(view: View) {
//        // Log and toast
//        Log.w("LogNotification", "requestPermission")
//
//        try {
//            // This is only necessary for API level >= 33 (TIRAMISU)
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//                if (ContextCompat.checkSelfPermission(
//                        this,
//                        android.Manifest.permission.POST_NOTIFICATIONS
//                    ) ==
//                    PackageManager.PERMISSION_GRANTED
//                ) {
//                    // FCM SDK (and your app) can post notifications.
//                } else if (shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS)) {
//                    Log.w("LogNotification", "vao else")
//                } else {
//                    // Directly ask for the permission
//                    requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
//                }
//            } else {
//                FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
//                    if (!task.isSuccessful) {
//                        Log.w(
//                            "LogNotification",
//                            "Fetching FCM registration token failed",
//                            task.exception
//                        )
//                        return@OnCompleteListener
//                    }
//
//                    val token = task.result
//
//                    Log.w("LogNotification", token)
//
//                })
//            }
//        }catch (e: IOException){
//            Log.d("errr", e.message.toString())
//        }
//
//
//    }
//
//    private val requestPermissionLauncher = registerForActivityResult(
//        ActivityResultContracts.RequestPermission(),
//    ) { isGranted: Boolean ->
//        if (isGranted) {
//            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
//                if (!task.isSuccessful) {
//                    Log.w(
//                        "LogNotification",
//                        "Fetching FCM registration token failed",
//                        task.exception
//                    )
//                    return@OnCompleteListener
//                }
//
//                val token = task.result
//
//
//                Log.d("123", token)
//            })
//        } else {
//            Log.w("LogNotification", "vao else")
//
//        }
//    }
//}


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun requestPermission(view: View) {
        Log.d("LogNotification", "requestPermission called")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                retrieveToken()
            } else if (shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS)) {
                Log.w("LogNotification", "Permission rationale displayed")
            } else {
                requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        } else {
            retrieveToken()
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Log.d("LogNotification", "Permission granted")
            retrieveToken()
        } else {
            Log.w("LogNotification", "Permission denied")
        }
    }

    private fun retrieveToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("LogNotification", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get the FCM token
            val token = task.result
            Log.d("LogNotification", "FCM Token: $token")
        })
    }
}

