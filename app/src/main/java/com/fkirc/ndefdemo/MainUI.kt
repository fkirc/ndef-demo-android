package com.fkirc.ndefdemo

import android.app.Activity
import android.graphics.Typeface
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.TextView
import androidx.core.content.ContextCompat.getSystemService


class MainUI(val ac: Activity) {

    private lateinit var mainTextView: TextView

    fun onCreate() {
        ac.setContentView(R.layout.activity_main)
        mainTextView = ac.findViewById(R.id.main_text_view)

        if (isNfcAvailable()) {
            setMainText(ac.getString(R.string.nfc_available_launch_text))
        } else {
            setMainText(ac.getString(R.string.nfc_not_available))
        }
    }

    fun showNdefData(msg: NdefMessage) {

        val records = msg.records

        val sb = StringBuilder()
        sb.appendln("NdefMessage received")
        sb.appendln("Number of Ndef records: " + records.size)

        records.forEachIndexed { idx, rec ->
            sb.appendln("Record " + idx.toString())
            sb.appendln(rec.toString())
        }

        sb.appendln()
        sb.appendln("Raw NdefMessage")
        sb.appendln(msg.toString())

        vibratePhone()
    }

    fun onUnsupportedTag() {
        setMainText(ac.getString(R.string.nfc_tag_not_supported))
        vibratePhone()
    }


    private fun setMainText(s: String, largeText: Boolean = true) {
        mainTextView.setText(s)
        if (largeText) {
            mainTextView.setTypeface(null, Typeface.BOLD)
            mainTextView.textSize = 23.toFloat()
        } else {
            mainTextView.setTypeface(null, Typeface.NORMAL)
            mainTextView.textSize = 18.toFloat()
        }
    }


    private fun vibratePhone() {
        val VIBRATE_DURATION = 500.toLong()
        val v = getSystemService(ac, Vibrator::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v!!.vibrate(VibrationEffect.createOneShot(VIBRATE_DURATION, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            //deprecated in API 26
            v!!.vibrate(VIBRATE_DURATION)
        }
    }


    private fun isNfcAvailable() : Boolean {
        val a = NfcAdapter.getDefaultAdapter(ac) ?: return false
        return a.isEnabled
    }
}
