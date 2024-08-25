package com.example.iot_object_reminder

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
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
        // 다이얼로그 빌더 생성
        val builder = AlertDialog.Builder(this)
        val inflater: LayoutInflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_register_item, null)
        builder.setView(dialogView)
        val dialog = builder.create()

        // 버튼 및 텍스트뷰 참조
        val btnBody: Button = dialogView.findViewById(R.id.btnBody)
        val btnItem: Button = dialogView.findViewById(R.id.btnItem)
        val tvDialogTitle: TextView = dialogView.findViewById(R.id.tvDialogTitle)

        // "본체" 버튼 클릭 처리
        btnBody.setOnClickListener {
            tvDialogTitle.text = "RFID를 인식 시켜주세요."
            btnBody.visibility = Button.GONE
            btnItem.visibility = Button.GONE
        }

        // "물건" 버튼 클릭 처리
        btnItem.setOnClickListener {
            tvDialogTitle.text = "RFID를 인식 시켜주세요."
            btnBody.visibility = Button.GONE
            btnItem.visibility = Button.GONE
        }

        // 다이얼로그 표시
        dialog.show()
    }
}
