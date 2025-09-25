package com.example.easyapp

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.easyapp.scanner.BarcodeScanner
import com.example.easyapp.ui.theme.EasyAppTheme

class MainActivity : ComponentActivity() {
    private lateinit var barcodeScanner: BarcodeScanner
    private var scannedBarcodes = mutableStateOf(listOf<String>())
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // バーコードスキャナの初期化
        barcodeScanner = BarcodeScanner(this) { barcode ->
            // スキャンされたバーコードをリストに追加
            scannedBarcodes.value = scannedBarcodes.value + barcode
        }
        
        setContent {
            EasyAppTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MainScreen(
                        scannedBarcodes = scannedBarcodes.value,
                        onOpenLegacy = {
                            startActivity(Intent(this, LegacyActivity::class.java))
                        },
                        onSimulateScan = { barcode ->
                            barcodeScanner.simulateBarcodeScan(barcode)
                        },
                        onClearBarcodes = {
                            scannedBarcodes.value = emptyList()
                        }
                    )
                }
            }
        }
    }
    
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        // バーコードスキャナのキーイベント処理
        if (barcodeScanner.handleKeyEvent(keyCode, event)) {
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}

@Composable
fun MainScreen(
    scannedBarcodes: List<String>,
    onOpenLegacy: () -> Unit,
    onSimulateScan: (String) -> Unit,
    onClearBarcodes: () -> Unit
) {
    var count by remember { mutableStateOf(0) }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // カウンタセクション
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "カウンター",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Count: $count",
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(onClick = { count++ }) { Text("+1") }
                    Button(onClick = { if (count > 0) count-- }) { Text("-1") }
                }
                Spacer(Modifier.height(12.dp))
                OutlinedButton(onClick = onOpenLegacy) {
                    Text("XML画面を開く")
                }
            }
        }
        
        // バーコードスキャンセクション
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "バーコードスキャン",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(8.dp))
                
                // テスト用ボタン群
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = { onSimulateScan("1234567890123") },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("テストスキャン1")
                    }
                    Button(
                        onClick = { onSimulateScan("9876543210987") },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("テストスキャン2")
                    }
                }
                Spacer(Modifier.height(8.dp))
                OutlinedButton(
                    onClick = onClearBarcodes,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("リストをクリア")
                }
                
                Spacer(Modifier.height(12.dp))
                
                // スキャン結果リスト
                if (scannedBarcodes.isNotEmpty()) {
                    Text(
                        text = "スキャン結果 (${scannedBarcodes.size}件)",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(Modifier.height(8.dp))
                    LazyColumn(
                        modifier = Modifier.heightIn(max = 200.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(scannedBarcodes.reversed()) { barcode ->
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer
                                )
                            ) {
                                Text(
                                    text = barcode,
                                    modifier = Modifier.padding(12.dp),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                } else {
                    Text(
                        text = "バーコードをスキャンしてください",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
        
        // 説明テキスト
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text(
                    text = "ハンディターミナル使用時の注意",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "• バーコードをスキャンすると自動で上記リストに追加されます\n" +
                           "• キーボード入力型の場合、連続入力+Enterで検出されます\n" +
                           "• DataWedgeやメーカー独自サービスにも対応しています",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
