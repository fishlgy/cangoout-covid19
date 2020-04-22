package com.codigo.canigoout.splash

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.codigo.canigoout.R
import com.codigo.canigoout.common.Outcome
import com.codigo.canigoout.location.LocationActivity
import com.codigo.canigoout.main.MainActivity
import org.koin.androidx.viewmodel.ext.android.viewModel


class SplashActivity : AppCompatActivity() {

    private val viewModel: SplashViewModel by viewModel<SplashViewModel>()

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, SplashActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        viewModel.hasNricExists()

        viewModel.checkExistRecordOutcome.observe(this, Observer {
                outcome ->
            when(outcome) {
                is Outcome.Success -> {
                    if(outcome.data) {
                        LocationActivity.start(this)
                    } else
                        MainActivity.start(this)

                    finish()
                }
            }
        })
    }
}

