package com.students.portfolio

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ClassAdapter(private var items: List<ClassModel>, val click: (Int,String)->Unit)
    : RecyclerView.Adapter<ClassAdapter.VH>() {

    inner class VH(view: View): RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(android.R.id.text1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val c = items[position]
        holder.title.text = c.name
        holder.itemView.setOnClickListener { click(c.id, c.name) }
    }

    override fun getItemCount(): Int = items.size

    fun update(newItems: List<ClassModel>) {
        items = newItems
        notifyDataSetChanged()
    }
}
