package com.codigo.canigoout.main

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.codigo.canigoout.BuildConfig
import com.codigo.canigoout.R
import com.codigo.canigoout.common.Outcome
import com.codigo.canigoout.location.LocationActivity
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import timber.log.Timber

class MainActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {

    private val RC_FINE_LOCATION = 1001
    private val compositeDisposable = CompositeDisposable()
    private val viewModel: MainViewModel by viewModel<MainViewModel>()

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, MainActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (!EasyPermissions.hasPermissions(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            promptPermissionExplanation()
        }

        setup()

        viewModel.hasNricExists()
        viewModel.checkNricOutcome.observe(this, Observer {
            outcome ->
            when(outcome) {
                is Outcome.Progress -> {
                    if(outcome.loading) {
                        progressBar.visibility = View.VISIBLE
                        progressBar.animate()
                        etNric.isEnabled = false
                        btnNext.isClickable = false
                    } else {
                        progressBar.visibility = View.GONE
                        progressBar.clearAnimation()
                        etNric.isEnabled = true
                        btnNext.isClickable = true
                    }
                }

                is Outcome.Failure -> {
                    inputLayoutNric.isErrorEnabled = true
                    inputLayoutNric.errorIconDrawable = getDrawable(R.drawable.ic_warning_24dp)
                    inputLayoutNric.error = outcome.e.message.toString()
                }

                is Outcome.Success -> {
                    inputLayoutNric.isErrorEnabled = false
                    inputLayoutNric.errorIconDrawable = null
                    LocationActivity.start(this)
                    finish()
                }
            }
        })

        viewModel.checkExistRecordOutcome.observe(this, Observer {
            outcome ->
            when(outcome) {
                is Outcome.Success -> {
                    etNric.setText(outcome.data)
                }
            }
        })
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }

    private fun setup() {
        tvVersion.text = BuildConfig.VERSION_NAME + " (" + BuildConfig.VERSION_CODE + ")"

        etNric.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                inputLayoutNric.isErrorEnabled = false
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                inputLayoutNric.isErrorEnabled = false
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                inputLayoutNric.isErrorEnabled = false
            }

        })

        btnNext.setOnClickListener {
            if(etNric.text.isNotEmpty())
                viewModel.validateNRIC(etNric.text.toString())
        }
    }

    private fun promptPermissionExplanation() {
        val view = layoutInflater.inflate(R.layout.dialog_simple, null)

        view.findViewById<TextView>(R.id.tvDescription)?.text =
            getString(R.string.rationale_fine_location)

        AlertDialog.Builder(this)
            .setView(view)
            .setCancelable(false)
            .setPositiveButton(R.string.dialog_button_ok) { dialog, which ->
                requestLocationPermissions()
            }.show()
    }

    private fun requestLocationPermissions() {
        EasyPermissions.requestPermissions(
            this,
            getString(R.string.rationale_fine_location),
            RC_FINE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
            promptPermissionExplanation()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        if (requestCode == RC_FINE_LOCATION &&
            perms[0] == Manifest.permission.ACCESS_FINE_LOCATION
        ) {
            Timber.d("Permissions granted")
        }
    }
}

