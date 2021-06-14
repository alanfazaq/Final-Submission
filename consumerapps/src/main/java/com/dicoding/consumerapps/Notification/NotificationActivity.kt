package com.dicoding.consumerapps.Notification

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import android.view.View
import android.widget.Switch
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import com.dicoding.consumerapps.R
import com.dicoding.consumerapps.databinding.ActivityNotificationBinding

class NotificationActivity : AppCompatActivity() {

    companion object {
        const val namePref = "SettingPref"
        private const val notify_Daily = "daily"
    }

    private lateinit var receiverAlarm: ReceiverAlarm
    private lateinit var mSharedPreferences: SharedPreferences
    private lateinit var bindingSetting: ActivityNotificationBinding

    private var switchCompat: SwitchCompat?= null
    internal lateinit var darkSharedPreferences: SharedPref


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingSetting = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(bindingSetting.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = getString(R.string.setting)

        receiverAlarm = ReceiverAlarm()
        mSharedPreferences = getSharedPreferences(namePref, Context.MODE_PRIVATE)

        setSwitch()
        bindingSetting.dailyNotif.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) { receiverAlarm.setDailyReminder(this, ReceiverAlarm.TYPE_Remind, getString(R.string.daily_message))
            } else {
                receiverAlarm.cancelAlarm(this)
            }
            saveChange(isChecked)
        }

        darkSharedPreferences = SharedPref(this)
        if (darkSharedPreferences.loadNightModeState() == true){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        val darkMode =  findViewById<SwitchCompat>(R.id.darkMode)
        switchCompat = darkMode as SwitchCompat
        if (darkSharedPreferences.loadNightModeState() == true){
            switchCompat!!.isChecked = true
        }

        switchCompat!!.setOnCheckedChangeListener{_, isChecked ->
            if (isChecked){
                darkSharedPreferences.setNightModeState(true)
                restartApp()
            } else{
                darkSharedPreferences.setNightModeState(false)
                restartApp()
            }
        }


    }
    fun restartApp(){
        val i = Intent(getApplicationContext(), NotificationActivity::class.java)
        startActivity(i)
        finish()
    }


    private fun setSwitch() {
        bindingSetting.dailyNotif.isChecked = mSharedPreferences.getBoolean(notify_Daily, false)
    }

    private fun saveChange(value: Boolean) {
        val editor = mSharedPreferences.edit()
        editor.putBoolean(notify_Daily, value)
        editor.apply()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }
    fun changeLanguage(v: View?) {
        val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
        startActivity(mIntent)
    }
}