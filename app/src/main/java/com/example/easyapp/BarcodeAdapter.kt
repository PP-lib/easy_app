package com.example.easyapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BarcodeAdapter(private val barcodes: List<String>) : 
    RecyclerView.Adapter<BarcodeAdapter.BarcodeViewHolder>() {
    
    inner class BarcodeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textBarcode: TextView = itemView.findViewById(android.R.id.text1)
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BarcodeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)
        return BarcodeViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: BarcodeViewHolder, position: Int) {
        holder.textBarcode.text = barcodes[position]
    }
    
    override fun getItemCount(): Int = barcodes.size
}