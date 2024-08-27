package com.example.iot_object_reminder

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.imageTintList = ColorStateList.valueOf(Color.WHITE)
        fab.setOnClickListener {
            // 다이얼로그 생성
            showCustomDialog()
        }
    }

    private fun showCustomDialog() {
        val builder = AlertDialog.Builder(this)
        val inflater: LayoutInflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_register_item, null)
        builder.setView(dialogView)
        val dialog = builder.create()

        // 다이얼로그 배경을 투명하게 설정
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)  // 어두워지는 효과 제거

        // 버튼 클릭 처리
        val btnBody: Button = dialogView.findViewById(R.id.btnBody)
        val btnItem: Button = dialogView.findViewById(R.id.btnItem)

        btnBody.setOnClickListener {
            Toast.makeText(this, "본체 선택", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        btnItem.setOnClickListener {
            Toast.makeText(this, "물건 선택", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        dialog.show()
    }

}
