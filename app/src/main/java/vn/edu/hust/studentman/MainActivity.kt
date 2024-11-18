package vn.edu.hust.studentman

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

  private val students = mutableListOf<StudentModel>()
  private lateinit var studentAdapter: StudentAdapter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    // Prepopulate with sample data
    students.addAll(listOf(
      StudentModel("Nguyễn Văn An", "SV001"),
      StudentModel("Trần Thị Bảo", "SV002")
      // Add more sample data as needed
    ))

    studentAdapter = StudentAdapter(
      students,
      onEdit = { showEditStudentDialog(it) },
      onRemove = { showDeleteConfirmationDialog(it) }
    )

    findViewById<RecyclerView>(R.id.recycler_view_students).apply {
      adapter = studentAdapter
      layoutManager = LinearLayoutManager(this@MainActivity)
    }

    findViewById<Button>(R.id.btn_add_new).setOnClickListener {
      showAddStudentDialog()
    }
  }

  // Function to show dialog for adding a new student
  private fun showAddStudentDialog() {
    val dialogView = layoutInflater.inflate(R.layout.dialog_add_edit_student, null)
    val editTextName = dialogView.findViewById<EditText>(R.id.editTextStudentName)
    val editTextId = dialogView.findViewById<EditText>(R.id.editTextStudentId)

    AlertDialog.Builder(this)
      .setTitle("Add New Student")
      .setView(dialogView)
      .setPositiveButton("Add") { _, _ ->
        val newStudent = StudentModel(editTextName.text.toString(), editTextId.text.toString())
        students.add(newStudent)
        studentAdapter.notifyItemInserted(students.size - 1)
      }
      .setNegativeButton("Cancel", null)
      .show()
  }

  // Function to show dialog for editing a student's info
  private fun showEditStudentDialog(student: StudentModel) {
    val dialogView = layoutInflater.inflate(R.layout.dialog_add_edit_student, null)
    val editTextName = dialogView.findViewById<EditText>(R.id.editTextStudentName)
    val editTextId = dialogView.findViewById<EditText>(R.id.editTextStudentId)

    editTextName.setText(student.studentName)
    editTextId.setText(student.studentId)

    AlertDialog.Builder(this)
      .setTitle("Edit Student")
      .setView(dialogView)
      .setPositiveButton("Save") { _, _ ->
        student.studentName = editTextName.text.toString()
        student.studentId = editTextId.text.toString()
        studentAdapter.notifyDataSetChanged()
      }
      .setNegativeButton("Cancel", null)
      .show()
  }

  // Function to show confirmation dialog for deletion
  private fun showDeleteConfirmationDialog(student: StudentModel) {
    AlertDialog.Builder(this)
      .setTitle("Delete Student")
      .setMessage("Are you sure you want to delete ${student.studentName}?")
      .setPositiveButton("Delete") { _, _ ->
        val position = students.indexOf(student)
        students.removeAt(position)
        studentAdapter.notifyItemRemoved(position)

        Snackbar.make(findViewById(R.id.main), "Student deleted", Snackbar.LENGTH_LONG)
          .setAction("Undo") {
            students.add(position, student)
            studentAdapter.notifyItemInserted(position)
          }
          .show()
      }
      .setNegativeButton("Cancel", null)
      .show()
  }
}
