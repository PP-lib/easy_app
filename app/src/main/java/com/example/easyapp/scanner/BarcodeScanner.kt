package com.example.easyapp.scanner

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.view.KeyEvent
import androidx.activity.ComponentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

/**
 * バーコードスキャナ統合クラス
 * ハンディターミナルのバーコードスキャナからの入力を統一的に処理する
 */
class BarcodeScanner(
    private val context: ComponentActivity,
    private val onBarcodeScanned: (String) -> Unit
) : LifecycleObserver {

    private var scanBuffer = StringBuilder()
    private val scanTimeoutMs = 100L // キー入力間隔でバーコード判定
    private var lastKeyTime = 0L
    
    private val keyEventReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let { processDataWedgeIntent(it) }
        }
    }
    
    init {
        context.lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        // DataWedge系のBroadcast受信を開始
        val filter = IntentFilter().apply {
            addAction("com.symbol.datawedge.api.RESULT_ACTION")
            addAction("com.honeywell.decode.intent.action.DECODE_DATA")
            addAction("android.intent.ACTION_DECODE_DATA")
        }
        context.registerReceiver(keyEventReceiver, filter)
    }
    
    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        context.unregisterReceiver(keyEventReceiver)
    }

    /**
     * キーイベント処理（多くのハンディターミナルはスキャン結果をキーイベントとして送信）
     */
    fun handleKeyEvent(keyCode: Int, event: KeyEvent?): Boolean {
        if (event?.action != KeyEvent.ACTION_DOWN) return false
        
        val currentTime = System.currentTimeMillis()
        
        when (keyCode) {
            KeyEvent.KEYCODE_ENTER -> {
                // Enterキーでバーコード読み取り完了とみなす
                if (scanBuffer.isNotEmpty()) {
                    val barcode = scanBuffer.toString()
                    scanBuffer.clear()
                    onBarcodeScanned(barcode)
                    return true
                }
            }
            else -> {
                // 一定時間内の連続入力はバーコードスキャンとみなす
                if (currentTime - lastKeyTime > scanTimeoutMs) {
                    scanBuffer.clear()
                }
                
                val char = event?.unicodeChar?.toChar()
                if (char != null && char.isDigit() || char?.isLetter() == true) {
                    scanBuffer.append(char)
                    lastKeyTime = currentTime
                }
            }
        }
        return false
    }
    
    /**
     * DataWedge等のIntent処理
     */
    private fun processDataWedgeIntent(intent: Intent) {
        when (intent.action) {
            "com.symbol.datawedge.api.RESULT_ACTION" -> {
                val data = intent.getStringExtra("com.symbol.datawedge.data_string")
                data?.let { onBarcodeScanned(it) }
            }
            "com.honeywell.decode.intent.action.DECODE_DATA" -> {
                val data = intent.getStringExtra("data")
                data?.let { onBarcodeScanned(it) }
            }
            "android.intent.ACTION_DECODE_DATA" -> {
                val data = intent.getStringExtra("barcode_string")
                data?.let { onBarcodeScanned(it) }
            }
        }
    }
    
    /**
     * 手動でバーコード読み取りをシミュレート（テスト用）
     */
    fun simulateBarcodeScan(barcode: String) {
        onBarcodeScanned(barcode)
    }
}