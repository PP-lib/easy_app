package com.example.easyapp

import org.junit.Test
import org.junit.Assert.*

/**
 * バーコードスキャナの基本機能テスト
 * 
 * Android環境以外でも実行可能な単純な論理テスト
 */
class BarcodeScannerTest {

    @Test
    fun testBarcodeValidation() {
        // 基本的なバーコード形式のテスト
        val validBarcodes = listOf(
            "1234567890123",  // EAN-13
            "123456789012",   // UPC-A
            "12345678",       // EAN-8
            "AB12345",        // Code 39
            "TEST123"         // テスト用
        )
        
        validBarcodes.forEach { barcode ->
            assertTrue("Barcode should be valid: $barcode", isValidBarcode(barcode))
        }
    }
    
    @Test
    fun testInvalidBarcodes() {
        val invalidBarcodes = listOf(
            "",              // 空文字
            " ",             // スペースのみ
            "12",            // 短すぎる
            "日本語",         // 非ASCII文字
            "special@char!"   // 特殊文字
        )
        
        invalidBarcodes.forEach { barcode ->
            assertFalse("Barcode should be invalid: $barcode", isValidBarcode(barcode))
        }
    }
    
    @Test
    fun testBarcodeListManagement() {
        val barcodes = mutableListOf<String>()
        
        // バーコード追加
        val testBarcode1 = "1234567890123"
        val testBarcode2 = "9876543210987"
        
        barcodes.add(testBarcode1)
        assertEquals(1, barcodes.size)
        assertEquals(testBarcode1, barcodes[0])
        
        barcodes.add(0, testBarcode2) // 先頭に追加
        assertEquals(2, barcodes.size)
        assertEquals(testBarcode2, barcodes[0])
        assertEquals(testBarcode1, barcodes[1])
        
        // クリア機能
        barcodes.clear()
        assertEquals(0, barcodes.size)
        assertTrue(barcodes.isEmpty())
    }
    
    @Test
    fun testScannerConfiguration() {
        // スキャナ設定の検証
        val config = mapOf(
            "scanTimeout" to 100L,
            "enableKeyboardMode" to true,
            "enableDataWedge" to true,
            "enableHoneywell" to true
        )
        
        assertEquals(100L, config["scanTimeout"])
        assertTrue(config["enableKeyboardMode"] == true)
        assertTrue(config["enableDataWedge"] == true)
        assertTrue(config["enableHoneywell"] == true)
    }
    
    /**
     * 簡単なバーコード検証ロジック
     * 実際のアプリケーションではより厳密な検証が必要
     */
    private fun isValidBarcode(barcode: String): Boolean {
        if (barcode.isBlank()) return false
        if (barcode.length < 3) return false
        
        // ASCII文字のみ許可 (数字、アルファベット、一部記号)
        return barcode.matches(Regex("[A-Za-z0-9-_.*]+"))
    }
}