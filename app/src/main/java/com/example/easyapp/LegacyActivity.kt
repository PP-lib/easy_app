package com.example.easyapp

import android.os.Bundle
import android.view.KeyEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.easyapp.databinding.ActivityLegacyBinding
import com.example.easyapp.scanner.BarcodeScanner

class LegacyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLegacyBinding
    private lateinit var barcodeScanner: BarcodeScanner
    private var count = 0
    private val scannedBarcodes = mutableListOf<String>()
    private lateinit var barcodeAdapter: BarcodeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLegacyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupBarcodeScanner()
        setupRecyclerView()
        setupButtons()
    }
    
    private fun setupBarcodeScanner() {
        barcodeScanner = BarcodeScanner(this) { barcode ->
            runOnUiThread {
                scannedBarcodes.add(0, barcode) // 先頭に追加
                barcodeAdapter.notifyItemInserted(0)
                binding.recyclerViewBarcodes.smoothScrollToPosition(0)
                updateBarcodeCount()
                
                Toast.makeText(this, "バーコード読取: $barcode", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    private fun setupRecyclerView() {
        barcodeAdapter = BarcodeAdapter(scannedBarcodes)
        binding.recyclerViewBarcodes.apply {
            layoutManager = LinearLayoutManager(this@LegacyActivity)
            adapter = barcodeAdapter
        }
    }
    
    private fun setupButtons() {
        binding.buttonIncrement.setOnClickListener {
            count++
            binding.textMessage.text = "Legacy Count: $count"
        }
        
        binding.buttonDecrement.setOnClickListener {
            if (count > 0) count--
            binding.textMessage.text = "Legacy Count: $count"
        }
        
        binding.buttonTestScan.setOnClickListener {
            val testBarcode = "TEST${System.currentTimeMillis()}"
            barcodeScanner.simulateBarcodeScan(testBarcode)
        }
        
        binding.buttonClearBarcodes.setOnClickListener {
            scannedBarcodes.clear()
            barcodeAdapter.notifyDataSetChanged()
            updateBarcodeCount()
        }
    }
    
    private fun updateBarcodeCount() {
        binding.textBarcodeCount.text = "スキャン済み: ${scannedBarcodes.size}件"
    }
    
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (barcodeScanner.handleKeyEvent(keyCode, event)) {
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}
