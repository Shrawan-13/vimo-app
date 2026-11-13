package com.teamshryne.vimo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MediaAdapter(
    private val items: MutableList<MediaItem>,
    private val onClick: (MediaItem) -> Unit,
) : RecyclerView.Adapter<MediaAdapter.VH>() {
    class VH(v: View) : RecyclerView.ViewHolder(v) { val text: TextView = v as TextView }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
        return VH(v)
    }
    override fun getItemCount() = items.size
    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]
        holder.text.text = java.io.File(item.path).name
        holder.itemView.setOnClickListener { onClick(item) }
    }
}
