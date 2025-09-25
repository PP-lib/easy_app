package com.example.easyapp

import android.os.Bundle
import android.view.KeyEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.easyapp.ui.theme.EasyAppTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

// --- Scan Buffer (簡易PoC) ----------------------------------------------------
private object ScanBuffer {
    private val builder = StringBuilder()
    private var lastTime = 0L
    private const val RESET_THRESHOLD_MS = 350L

    fun handle(event: KeyEvent): String? {
        if (event.action != KeyEvent.ACTION_DOWN) return null
        val now = System.currentTimeMillis()
        if (now - lastTime > RESET_THRESHOLD_MS) builder.clear()
        lastTime = now
        return when (event.keyCode) {
            KeyEvent.KEYCODE_ENTER -> builder.toString().also { builder.clear() }.takeIf { it.isNotBlank() }
            KeyEvent.KEYCODE_DEL -> { if (builder.isNotEmpty()) builder.deleteAt(builder.lastIndex); null }
            else -> {
                val ch = event.unicodeChar
                if (ch != 0 && !Character.isISOControl(ch)) builder.append(ch.toChar())
                null
            }
        }
    }
}

// --- ViewModel 代替 (PoCなので手作り) -----------------------------------------
data class ScanUiState(
    val lastCode: String = "",
    val history: List<String> = emptyList()
)

class ScanStateHolder {
    private val _state = MutableStateFlow(ScanUiState())
    val state: StateFlow<ScanUiState> = _state

    fun onScanned(code: String) = _state.update {
        it.copy(
            lastCode = code,
            history = (listOf(code) + it.history).take(50)
        )
    }
}

class MainActivity : ComponentActivity() {
    private val scanHolder = ScanStateHolder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EasyAppTheme {
                Surface(modifier = Modifier.fillMaxSize()) { ScanScreen(scanHolder) }
            }
        }
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        val scanned = ScanBuffer.handle(event)
        if (scanned != null) {
            scanHolder.onScanned(scanned)
            // ここでサウンドやバイブを追加可能
            return true
        }
        return super.dispatchKeyEvent(event)
    }
}

@Composable
private fun ScanScreen(holder: ScanStateHolder) {
    val ui by holder.state.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text("Last Code", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(4.dp))
        Surface(tonalElevation = 2.dp, shape = MaterialTheme.shapes.small) {
            Text(
                ui.lastCode.ifBlank { "(none)" },
                modifier = Modifier.padding(12.dp),
                style = MaterialTheme.typography.headlineSmall
            )
        }
        Spacer(Modifier.height(16.dp))
        Text("History (${ui.history.size})", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        Column(Modifier.weight(1f).fillMaxWidth()) {
            ui.history.forEach { code ->
                Text(code, modifier = Modifier.padding(vertical = 2.dp))
            }
        }
        Divider()
        Text(
            "Enter付きのバーコード入力(=キーボード入力)で自動確定。" +
                " 放置間隔>${ScanBuffer::class.java.getDeclaredField("RESET_THRESHOLD_MS").let { 350 }}msでバッファリセット。",
            style = MaterialTheme.typography.bodySmall
        )
    }
}
