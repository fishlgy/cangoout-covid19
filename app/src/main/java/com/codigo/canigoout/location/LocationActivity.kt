package com.codigo.canigoout.location

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.codigo.canigoout.BuildConfig
import com.codigo.canigoout.R
import com.codigo.canigoout.common.Outcome
import com.codigo.canigoout.main.MainActivity
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_location.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class LocationActivity : AppCompatActivity() {

    lateinit var player: MediaPlayer
    private val compositeDisposable = CompositeDisposable()
    private val viewModel: LocationViewModel by viewModel<LocationViewModel>()

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, LocationActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)

        player = MediaPlayer.create(this, resources.getIdentifier("astrominia_coffin", "raw", packageName))

        setup()

        viewModel.locationOutcome.observe(this, Observer { outcome ->
            when (outcome) {
                is Outcome.Progress -> {
                    if (outcome.loading) {
                        progressBar.visibility = View.VISIBLE
                        progressBar.animate()
                    } else {
                        progressBar.visibility = View.GONE
                        progressBar.clearAnimation()
                    }
                }

                is Outcome.Success -> {
                    if(outcome.data) {
                        layoutOk.visibility = View.VISIBLE
                        layoutNotOk.visibility = View.GONE
                    } else {
                        layoutOk.visibility = View.GONE
                        layoutNotOk.visibility = View.VISIBLE
                    }
                }
            }
        })

        viewModel.backOutcome.observe(this, Observer {
            MainActivity.start(this)
        })
    }

    override fun onDestroy() {
        player.stop()
        compositeDisposable.clear()
        super.onDestroy()
    }

    private fun setup() {
        viewModel.canIGoOut()

        tvVersion.text = BuildConfig.VERSION_NAME + " (" + BuildConfig.VERSION_CODE + ")"

        tvBack.setOnClickListener {
            player.stop()
            viewModel.goBack()
        }

        tvIdc.setOnClickListener {

            val imageResId = resources.getIdentifier("coffin2", "raw", packageName)
            val imageResId2 = resources.getIdentifier("coffin", "raw", packageName)
            val imageResId3 = resources.getIdentifier("coffin3", "raw", packageName)

            Glide.with(this)
                .load(imageResId)
                .into(DrawableImageViewTarget(gifView))

            val handler = Handler()
            var flag = 0

            val changeImageRunnable: Runnable = object : Runnable {
                override fun run() {
                    if (flag > 1)
                        handler.removeCallbacks(this)
                    else {
                        Glide.with(this@LocationActivity)
                            .load(imageResId2)
                            .into(DrawableImageViewTarget(gifView))
                        flag++
                    }
                }
            }

            val changeImageRunnable2: Runnable = object : Runnable {
                override fun run() {
                    if (flag > 1)
                        handler.removeCallbacks(this)
                    else {

                        player.seekTo(35000)
                        Glide.with(this@LocationActivity)
                            .load(imageResId3)
                            .into(DrawableImageViewTarget(gifView))
                        flag++
                    }
                }
            }

            handler.postDelayed(changeImageRunnable, 9500)
            handler.postDelayed(changeImageRunnable2, 16000)


            player.seekTo(12000)
            player.start()
        }
    }
}