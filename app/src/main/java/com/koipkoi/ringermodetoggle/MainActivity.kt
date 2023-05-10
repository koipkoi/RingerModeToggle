package com.koipkoi.ringermodetoggle

import android.app.NotificationManager
import android.content.Intent
import android.media.AudioManager
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
  private lateinit var audioManager: AudioManager
  private lateinit var notificationManager: NotificationManager

  private val permissionRequestLauncher =
    registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
      if (notificationManager.isNotificationPolicyAccessGranted) {
        toggle()
        return@registerForActivityResult
      }

      toast("알림 권한을 허용해주세요.")
      finish()
    }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    audioManager = getSystemService(AudioManager::class.java)
    notificationManager = getSystemService(NotificationManager::class.java)

    if (notificationManager.isNotificationPolicyAccessGranted) {
      val intent = Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
      permissionRequestLauncher.launch(intent)
      return
    }

    toggle()
  }

  @Suppress("SameParameterValue")
  private fun toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
  }

  private fun toggle() {
    if (audioManager.ringerMode == AudioManager.RINGER_MODE_VIBRATE) {
      audioManager.ringerMode = AudioManager.RINGER_MODE_NORMAL
    } else {
      audioManager.ringerMode = AudioManager.RINGER_MODE_VIBRATE
    }

    finish()
  }
}
