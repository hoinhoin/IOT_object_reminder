package com.example.iot_object_reminder

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

class ScheduleDialogManager(private val context: Context) {

    fun showScheduleEditDialog() {
        val builder = AlertDialog.Builder(context)
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val dialogView = inflater.inflate(R.layout.dialog_schedule_edit, null)
        builder.setView(dialogView)
        val dialog = builder.create()

        val btnCalendar: Button = dialogView.findViewById(R.id.button_calendar)
        val btnDay: Button = dialogView.findViewById(R.id.button_day)

        btnCalendar.setOnClickListener {
            Toast.makeText(context, "캘린더 선택", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        btnDay.setOnClickListener {
            val intent = Intent(context, WeekdayActivity::class.java)
            context.startActivity(intent)
            dialog.dismiss()
        }

        dialog.show()
    }
}
