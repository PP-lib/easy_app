package com.example.easyapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.easyapp.ui.theme.EasyAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EasyAppTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    CounterScreen(onOpenLegacy = {
                        startActivity(Intent(this, LegacyActivity::class.java))
                    })
                }
            }
        }
    }
}

@Composable
fun CounterScreen(onOpenLegacy: () -> Unit) {
    var count by remember { mutableStateOf(0) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Count: $count", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = { count++ }) { Text("+1") }
            Button(onClick = { if (count > 0) count-- }) { Text("-1") }
        }
        Spacer(Modifier.height(24.dp))
        OutlinedButton(onClick = onOpenLegacy) {
            Text("XML画面を開く")
        }
    }
}
