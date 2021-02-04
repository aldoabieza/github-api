package com.something.consumergithubapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.something.consumergithubapp.MainActivity
import com.something.consumergithubapp.R

class SplashScreen : AppCompatActivity() {

    private lateinit var handler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        handler = Handler().apply {
            postDelayed({
                val moveMainActivity = Intent(this@SplashScreen, MainActivity::class.java)
                startActivity(moveMainActivity)
                finish()
            }, 3000)
        }

    }
}