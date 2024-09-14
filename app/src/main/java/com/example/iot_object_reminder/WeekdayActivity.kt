package com.example.iot_object_reminder

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class WeekdayActivity : AppCompatActivity() {

    private var selectedTextView: TextView? = null
    private lateinit var dayTextViews: List<TextView> // 요일 텍스트 뷰들을 저장할 리스트

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weekday)

        // 툴바 설정
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "" // 제목을 빈 문자열로 설정

        // 요일 텍스트 뷰들을 리스트에 추가
        dayTextViews = listOf(
            findViewById(R.id.sun),
            findViewById(R.id.mon),
            findViewById(R.id.tue),
            findViewById(R.id.wed),
            findViewById(R.id.thu),
            findViewById(R.id.fri),
            findViewById(R.id.sat)
        )

        // 각 요일에 클릭 리스너 추가
        dayTextViews.forEach { textView ->
            textView.setOnClickListener { handleDaySelection(it as TextView) }
        }

        // 기본 선택 요일 설정 (예: 월요일)
        handleDaySelection(dayTextViews[1]) // 예를 들어 월요일을 기본으로 선택
    }

    // 요일 선택 시 처리하는 함수
    private fun handleDaySelection(selectedDay: TextView) {
        // 모든 요일의 배경을 초기화
        dayTextViews.forEach { it.setBackgroundResource(0) }

        // 선택된 요일의 배경을 원형으로 변경
        selectedDay.setBackgroundResource(R.drawable.weekday_selector)
        selectedTextView = selectedDay

        Toast.makeText(this, "${selectedDay.text} 선택됨", Toast.LENGTH_SHORT).show()
    }
}
