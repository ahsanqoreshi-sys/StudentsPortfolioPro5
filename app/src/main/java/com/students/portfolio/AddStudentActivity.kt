package com.students.portfolio

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class AddStudentActivity: AppCompatActivity() {
    lateinit var db: DBHelper
    var classId = 0
    var studentId = 0
    var photoUri: String? = null
    val PICK_IMAGE = 1001
    val CAMERA_REQ = 1002

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_student)
        db = DBHelper(this)
        classId = intent.getIntExtra("classId",0)
        studentId = intent.getIntExtra("studentId",0)

        val etName = findViewById<EditText>(R.id.etName)
        val etFather = findViewById<EditText>(R.id.etFather)
        val etClassSection = findViewById<EditText>(R.id.etClassSection)
        val etRoll = findViewById<EditText>(R.id.etRoll)
        val etPhone = findViewById<EditText>(R.id.etPhone)
        val etAddress = findViewById<EditText>(R.id.etAddress)
        val etGrade = findViewById<EditText>(R.id.etGrade)
        val etNotes = findViewById<EditText>(R.id.etNotes)
        val ivPhoto = findViewById<ImageView>(R.id.ivPhoto)
        val btnChoose = findViewById<Button>(R.id.btnChoose)
        val btnSave = findViewById<Button>(R.id.btnSave)

        if (studentId > 0) {
            val s = db.getStudentById(studentId)
            s?.let {
                etName.setText(it.name)
                etRoll.setText(it.rollNo.toString())
                etGrade.setText(it.grade)
                etNotes.setText(it.notes)
                photoUri = it.photoUri
                if (!photoUri.isNullOrEmpty()) ivPhoto.setImageURI(Uri.parse(photoUri))
            }
        }

        btnChoose.setOnClickListener {
            val items = arrayOf("Choose from gallery", "Take photo")
            androidx.appcompat.app.AlertDialog.Builder(this).setItems(items) { _, which ->
                when (which) {
                    0 -> {
                        val i = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                        startActivityForResult(i, PICK_IMAGE)
                    }
                    1 -> {
                        val i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        startActivityForResult(i, CAMERA_REQ)
                    }
                }
            }.show()
        }

        btnSave.setOnClickListener {
            val name = etName.text.toString().trim()
            val roll = etRoll.text.toString().toIntOrNull() ?: 0
            if (name.isEmpty()) { etName.error = "Enter name"; return@setOnClickListener }
            val father = etFather.text.toString().trim()
            val classSec = etClassSection.text.toString().trim()
            val phone = etPhone.text.toString().trim()
            val address = etAddress.text.toString().trim()
            val grade = etGrade.text.toString().trim()
            val notes = etNotes.text.toString().trim()
            if (studentId > 0) {
                db.updateStudent(Student(studentId, classId, name, roll, grade, photoUri, notes, father, classSec, phone, address))
            } else {
                db.insertStudent(Student(0, classId, name, roll, grade, photoUri, notes, father, classSec, phone, address))
            }
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode,resultCode,data)
        if (resultCode != Activity.RESULT_OK) return
        val iv = findViewById<ImageView>(R.id.ivPhoto)
        if (requestCode == PICK_IMAGE) {
            val uri = data?.data
            uri?.let { photoUri = it.toString(); iv.setImageURI(it) }
        } else if (requestCode == CAMERA_REQ) {
            val bitmap = data?.extras?.get("data") as? android.graphics.Bitmap
            val uri = FileUtil.saveBitmapToCache(this, bitmap)
            photoUri = uri?.toString()
            iv.setImageURI(uri)
        }
    }
}
