https://youtu.be/zmG2JMu9jmw?si=RGxXMKCngALCqByC

https://github.com/NguyenManh1404/AndroidKotlinNotificationn

https://console.firebase.google.com/u/2/project/my-application-notificat-c5f35/notification/compose?campaignId=8367979748430420055

https://firebase.google.com/docs/cloud-messaging/android/first-message

# Notification with Firebase

1. import firebase into application(tool Add)

2. **Create MyFirebaseMessagingService.kt**

```kt
package com.example.myapplication

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.*
import android.os.Build
import android.util.Log
import androidx.core.app.*
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
    private val CHANNEL_ID = "FirebaseNotifications"

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FirebaseService", "New token: $token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d("FirebaseService", "Message received: ${remoteMessage.data}")

        // Extract message payload
        val message = remoteMessage.data["message"] ?: "No message payload"

        // Log message
        Log.d("FirebaseService", "Notification Message: $message")

        // Show notification
        showNotification(message)
    }

    private fun showNotification(message: String) {
        // Create notification channel (for Android 8.0+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Firebase Notifications",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Channel for Firebase notifications"
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        // Build the notification
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info) // Replace with your app's icon
            .setContentTitle("New Notification")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        // Display the notification
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        NotificationManagerCompat.from(this).notify(1, notification)
    }
}


```

3. import **AndroidManifest.xml**

```xml
 <service
            android:name=".MyFirebaseMessagingService"
            android:exported="true">
            <intent-filter>
                <action android:name="com.google.firebase.MESSAGING_EVENT" />
            </intent-filter>
        </service>
        <meta-data
            android:name="com.google.firebase.messaging.default_notification_icon"
            android:resource="@drawable/ic_launcher_foreground" />

        <meta-data
            android:name="com.google.firebase.messaging.default_notification_color"
            android:resource="@color/black" />
        <meta-data
            android:name="com.google.firebase.messaging.default_notification_channel_id"
            android:value="@string/defaultNotificationChannelId" />

```

4. Import **MainActivity**

```kt
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



```
