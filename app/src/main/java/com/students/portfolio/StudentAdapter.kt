package com.students.portfolio

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class StudentAdapter(private var items: List<Student>,
                     val onEdit: (Student)->Unit,
                     val onDelete: (Student)->Unit)
    : RecyclerView.Adapter<StudentAdapter.VH>() {

    inner class VH(v: View): RecyclerView.ViewHolder(v) {
        val tvName: TextView = v.findViewById(R.id.tvName)
        val tvRoll: TextView = v.findViewById(R.id.tvRoll)
        val tvGrade: TextView = v.findViewById(R.id.tvGrade)
        val ivPhoto: ImageView = v.findViewById(R.id.ivPhoto)
        val btnEdit: View = v.findViewById(R.id.btnEdit)
        val btnDelete: View = v.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_student, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val s = items[position]
        holder.tvName.text = s.name
        holder.tvRoll.text = "Roll: ${s.rollNo}"
        holder.tvGrade.text = "Grade: ${s.grade ?: ""}"
        if (!s.photoUri.isNullOrEmpty()) {
            holder.ivPhoto.setImageURI(android.net.Uri.parse(s.photoUri))
        } else {
            holder.ivPhoto.setImageResource(android.R.drawable.sym_def_app_icon)
        }
        holder.btnEdit.setOnClickListener { onEdit(s) }
        holder.btnDelete.setOnClickListener { onDelete(s) }
    }

    override fun getItemCount(): Int = items.size

    fun update(newItems: List<Student>) {
        items = newItems
        notifyDataSetChanged()
    }
}
