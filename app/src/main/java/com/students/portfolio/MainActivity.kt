package com.students.portfolio

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity: AppCompatActivity() {
    lateinit var db: DBHelper
    lateinit var adapter: ClassAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        db = DBHelper(this)

        val etClass = findViewById<EditText>(R.id.etClassName)
        val btnCreate = findViewById<Button>(R.id.btnCreateClass)
        val btnExport = findViewById<Button>(R.id.btnExport)
        val rv = findViewById<RecyclerView>(R.id.rvClasses)
        rv.layoutManager = LinearLayoutManager(this)

        adapter = ClassAdapter(db.getAllClasses()) { classId, className ->
            val intent = Intent(this, ViewStudentsActivity::class.java)
            intent.putExtra("classId", classId)
            intent.putExtra("className", className)
            startActivity(intent)
        }
        rv.adapter = adapter

        btnCreate.setOnClickListener {
            val name = etClass.text.toString().trim()
            if (name.isEmpty()) { etClass.error = "Enter class name"; return@setOnClickListener }
            db.insertClass(name)
            etClass.setText("")
            refresh()
        }

        btnExport.setOnClickListener {
            val classes = db.getAllClasses()
            if (classes.isEmpty()) { showMessage("No classes available"); return@setOnClickListener }
            val names = classes.map { it.name }.toTypedArray()
            AlertDialog.Builder(this).setTitle("Export Class")
                .setItems(names) { _, which ->
                    val cls = classes[which]
                    val csv = db.exportClassToCSV(cls.id)
                    val file = FileUtil.saveTextToDownloads(this, "${cls.name.replace(" ","_")}.csv", csv)
                    showMessage("Exported to: ${file?.absolutePath ?: "failed"}")
                }.show()
        }
    }

    override fun onResume() { super.onResume(); refresh() }

    private fun refresh() { adapter.update(db.getAllClasses()) }

    private fun showMessage(msg: String) { AlertDialog.Builder(this).setMessage(msg).setPositiveButton("OK", null).show() }
}
