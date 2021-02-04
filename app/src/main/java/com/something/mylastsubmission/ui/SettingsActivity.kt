package com.something.mylastsubmission.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import com.something.mylastsubmission.R
import com.something.mylastsubmission.alarm.AlarmReceiver
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {

    private lateinit var alarmReceiver: AlarmReceiver

    companion object {
        private const val EXTRA_CONDITION = "extra_condition"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        alarmReceiver = AlarmReceiver()

        supportActionBar?.title = resources.getString(R.string.setting)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val sp = getSharedPreferences("SHAREDPREF_NAME", Context.MODE_PRIVATE)
        if (sp.getBoolean(EXTRA_CONDITION, false)){
            switch_reminder.isChecked = true
        }

        switch_reminder.setOnCheckedChangeListener { _, checked ->
            if (checked) {
                val spEdit = sp.edit()
                spEdit.putBoolean(EXTRA_CONDITION, true)
                spEdit.apply()
                alarmReceiver.setRepeatAlarm(
                        this,
                        AlarmReceiver.TYPE_REPEATING,
                        "09:00",
                        resources.getString(R.string.message)
                )
            }else{
                val spEditor= sp.edit()
                alarmReceiver.cancelAlarm(this, AlarmReceiver.TYPE_REPEATING)
                spEditor.clear()
                spEditor.apply()
            }
        }

        tv_translate.setOnClickListener {
            val intentTranslate = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(intentTranslate)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}