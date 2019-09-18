package com.fkirc.ndefdemo

import android.content.Intent
import android.content.IntentFilter
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.nfc.tech.Ndef
import android.nfc.tech.NfcA
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import android.app.PendingIntent


class MainActivity : AppCompatActivity() {

    private var adapter: NfcAdapter? = null
    private var pendingIntent: PendingIntent? = null

    private lateinit var mainUI : MainUI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainUI = MainUI(this)
        mainUI.onCreate()

        adapter = NfcAdapter.getDefaultAdapter(this)
        val intent = Intent(this, javaClass).apply {
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
    }

    override fun onResume() {
        super.onResume()
        val ndefIntentFilter = IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED).apply {
            addDataScheme("https")
            addDataScheme("http")
        }
        val intentFiltersArray = arrayOf(ndefIntentFilter)
        val techListsArray = arrayOf(arrayOf(Ndef::class.java.name, NfcA::class.java.name))
        adapter?.enableForegroundDispatch(this, pendingIntent, intentFiltersArray, techListsArray)
    }

    public override fun onPause() {
        super.onPause()
        adapter?.disableForegroundDispatch(this)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        val msg = retrieveNdefMessageFromIntent(intent)
        if (msg == null) {
            mainUI.onUnsupportedTag()
            return
        }

        mainUI.showNdefData(msg)
    }


    private fun retrieveNdefMessageFromIntent(intent: Intent?) : NdefMessage? {
        if (NfcAdapter.ACTION_NDEF_DISCOVERED != intent?.action) {
            return null
        }
        val rawMessages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES) ?: return null
        if (rawMessages.isEmpty()) {
            return null
        }
        val msg = rawMessages[0] // There is only one NdefMessage
        if (msg == null || msg !is NdefMessage) {
            return null
        }
        return msg
    }
}
