        package com.students.portfolio

        import android.content.ContentValues
        import android.content.Context
        import android.database.sqlite.SQLiteDatabase
        import android.database.sqlite.SQLiteOpenHelper

        data class ClassModel(val id:Int, val name:String)
        data class Student(val id:Int, val classId:Int, val name:String, val rollNo:Int, val grade:String?, val photoUri:String?, val notes:String?, val father:String?, val classSection:String?, val phone:String?, val address:String?)

        class DBHelper(ctx: Context): SQLiteOpenHelper(ctx, "students.db", null, 1) {
            val C = "classes"
            val S = "students"
            override fun onCreate(db: SQLiteDatabase) {
                db.execSQL("CREATE TABLE $C(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT)")
                db.execSQL("CREATE TABLE $S(id INTEGER PRIMARY KEY AUTOINCREMENT, classId INTEGER, name TEXT, roll INTEGER, grade TEXT, photo TEXT, notes TEXT, father TEXT, classSection TEXT, phone TEXT, address TEXT)")
            }
            override fun onUpgrade(db: SQLiteDatabase, oldV:Int, newV:Int) {}

            fun insertClass(name:String) {
                val db = writableDatabase
                val cv = ContentValues(); cv.put("name", name); db.insert(C, null, cv)
            }
            fun getAllClasses(): List<ClassModel> {
                val list = mutableListOf<ClassModel>()
                val db = readableDatabase
                val cur = db.rawQuery("SELECT id,name FROM $C ORDER BY id DESC", null)
                while (cur.moveToNext()) list.add(ClassModel(cur.getInt(0), cur.getString(1)))
                cur.close()
                return list
            }

            fun insertStudent(s: Student) {
                val db = writableDatabase
                val cv = ContentValues()
                cv.put("classId", s.classId); cv.put("name", s.name); cv.put("roll", s.rollNo)
                cv.put("grade", s.grade); cv.put("photo", s.photoUri); cv.put("notes", s.notes)
                cv.put("father", s.father); cv.put("classSection", s.classSection); cv.put("phone", s.phone); cv.put("address", s.address)
                db.insert(S, null, cv)
            }

            fun updateStudent(s: Student) {
                val db = writableDatabase
                val cv = ContentValues()
                cv.put("name", s.name); cv.put("roll", s.rollNo); cv.put("grade", s.grade)
                cv.put("photo", s.photoUri); cv.put("notes", s.notes); cv.put("father", s.father); cv.put("classSection", s.classSection); cv.put("phone", s.phone); cv.put("address", s.address)
                db.update(S, cv, "id=?", arrayOf(s.id.toString()))
            }

            fun deleteStudent(id:Int) { writableDatabase.delete(S, "id=?", arrayOf(id.toString())) }

            fun getStudentsByClass(classId:Int): List<Student> {
                val list = mutableListOf<Student>()
                val db = readableDatabase
                val cur = db.rawQuery("SELECT id,classId,name,roll,grade,photo,notes,father,classSection,phone,address FROM $S WHERE classId=? ORDER BY roll", arrayOf(classId.toString()))
                while (cur.moveToNext()) list.add(Student(cur.getInt(0), cur.getInt(1), cur.getString(2), cur.getInt(3), cur.getString(4), cur.getString(5), cur.getString(6), cur.getString(7), cur.getString(8), cur.getString(9), cur.getString(10)))
                cur.close()
                return list
            }
            fun getStudentById(id:Int): Student? {
                val db = readableDatabase
                val cur = db.rawQuery("SELECT id,classId,name,roll,grade,photo,notes,father,classSection,phone,address FROM $S WHERE id=?", arrayOf(id.toString()))
                if (cur.moveToFirst()) {
                    val s = Student(cur.getInt(0), cur.getInt(1), cur.getString(2), cur.getInt(3), cur.getString(4), cur.getString(5), cur.getString(6), cur.getString(7), cur.getString(8), cur.getString(9), cur.getString(10))
                    cur.close(); return s
                }
                cur.close(); return null
            }

            fun exportClassToCSV(classId:Int): String {
                val sb = StringBuilder()
                sb.append("Name,Roll,Grade,Notes,PhotoUri,Father,ClassSection,Phone,Address
")
                val students = getStudentsByClass(classId)
                for (s in students) {
                    sb.append(""${s.name}",${s.rollNo},"${s.grade ?: ""}","${s.notes ?: ""}","${s.photoUri ?: ""}","${s.father ?: ""}","${s.classSection ?: ""}","${s.phone ?: ""}","${s.address ?: ""}"
")
                }
                return sb.toString()
            }
        }
