package com.example.iot_object_reminder

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import java.util.*

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

        // 뒤로 가기 버튼 클릭 리스너 추가
        val backIcon = findViewById<ImageView>(R.id.back_icon)
        backIcon.setOnClickListener {
            finish()  // 현재 액티비티 종료
        }

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

        // 기본 선택 요일 설정 (오늘의 요일 선택)
        selectToday()
    }

    // 오늘 요일에 따라 UI의 요일 선택
    private fun selectToday() {
        val calendar = Calendar.getInstance()
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

        // Calendar.DAY_OF_WEEK returns 1 (Sunday) to 7 (Saturday), so map that to the correct index
        // Sunday is 1, Monday is 2, ..., Saturday is 7
        val dayIndex = when (dayOfWeek) {
            Calendar.SUNDAY -> 0
            Calendar.MONDAY -> 1
            Calendar.TUESDAY -> 2
            Calendar.WEDNESDAY -> 3
            Calendar.THURSDAY -> 4
            Calendar.FRIDAY -> 5
            Calendar.SATURDAY -> 6
            else -> 1 // Default to Monday if something goes wrong
        }

        handleDaySelection(dayTextViews[dayIndex])
    }

    // 요일 선택 시 처리하는 함수
    private fun handleDaySelection(selectedDay: TextView) {
        // 모든 요일의 배경을 초기화
        dayTextViews.forEach { it.setBackgroundResource(0) }

        selectedDay.setBackgroundResource(R.drawable.weekday_selector)
        selectedTextView = selectedDay

        Toast.makeText(this, "${selectedDay.text} 선택됨", Toast.LENGTH_SHORT).show()
    }
}
