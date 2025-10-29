package com.students.portfolio

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ViewStudentsActivity: AppCompatActivity() {
    lateinit var db: DBHelper
    var classId = 0
    lateinit var adapter: StudentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_students)
        db = DBHelper(this)
        classId = intent.getIntExtra("classId", 0)
        val className = intent.getStringExtra("className") ?: "Class"

        title = "Students — $className"

        val rv = findViewById<RecyclerView>(R.id.rvStudents)
        rv.layoutManager = LinearLayoutManager(this)
        adapter = StudentAdapter(db.getStudentsByClass(classId),
            onEdit = { s -> editStudent(s) },
            onDelete = { s -> deleteStudent(s) })
        rv.adapter = adapter

        val btnAdd = findViewById<Button>(R.id.btnAddStudent)
        btnAdd.setOnClickListener {
            val i = Intent(this, AddStudentActivity::class.java)
            i.putExtra("classId", classId)
            startActivity(i)
        }
    }

    override fun onResume() {
        super.onResume()
        adapter.update(db.getStudentsByClass(classId))
    }

    private fun editStudent(s: Student) {
        val i = Intent(this, AddStudentActivity::class.java)
        i.putExtra("classId", classId)
        i.putExtra("studentId", s.id)
        startActivity(i)
    }

    private fun deleteStudent(s: Student) {
        AlertDialog.Builder(this).setMessage("Delete ${s.name}?")
            .setPositiveButton("Yes") { _, _ ->
                db.deleteStudent(s.id)
                adapter.update(db.getStudentsByClass(classId))
            }.setNegativeButton("No", null).show()
    }
}
