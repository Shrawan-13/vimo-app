package com.teamshryne.vimo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TimelineAdapter : RecyclerView.Adapter<TimelineAdapter.VH>() {
    private val items = mutableListOf<TimelineClip>()
    fun setItems(newItems: List<TimelineClip>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
    class VH(v: View) : RecyclerView.ViewHolder(v) { val text: TextView = v as TextView }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
        return VH(v)
    }
    override fun getItemCount() = items.size
    override fun onBindViewHolder(holder: VH, position: Int) {
        val c = items[position]
        holder.text.text = "T${c.trackId} C${c.clipId}: ${c.startMs}ms-${c.endMs}ms (asset ${c.asset})"
    }
}
