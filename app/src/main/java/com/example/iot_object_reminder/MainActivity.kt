package com.example.iot_object_reminder

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import okhttp3.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var webSocket: WebSocket
    private var rfidData: String? = null // 수신한 RFID 정보를 저장할 변수
    private var rfidDialog: AlertDialog? = null // RFID 다이얼로그를 저장할 변수

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 웹소켓 연결 설정
        initWebSocket()

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.imageTintList = ColorStateList.valueOf(Color.WHITE)
        fab.setOnClickListener {
            // 첫 번째 다이얼로그를 표시하는 함수 호출
            showCustomDialog()
        }

        // 스케줄 편집 다이얼로그 호출
        val calendarIcon: ImageView = findViewById(R.id.calendar_icon)
        calendarIcon.setOnClickListener {
            showScheduleEditDialog()
        }
    }

    private fun initWebSocket() {
        val client = OkHttpClient.Builder()
            .readTimeout(0, TimeUnit.MILLISECONDS)
            .build()

        val request = Request.Builder()
            .url("ws://192.168.4.1:8080") // 웹소켓 서버 주소
            .build()

        val listener = object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "웹소켓 연결 성공", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                runOnUiThread {
                    rfidData = text // 수신한 RFID 데이터를 변수에 저장
                    // 이미 열린 RFID 다이얼로그가 있을 경우, 그 다이얼로그의 메시지를 업데이트
                    rfidDialog?.setMessage("수신된 RFID: $rfidData")
                }
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "웹소켓 오류: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                webSocket.close(1000, null)
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "웹소켓 연결 종료", Toast.LENGTH_SHORT).show()
                }
            }
        }

        webSocket = client.newWebSocket(request, listener)
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
            // 본체 선택 후 RFID 인식 다이얼로그 표시
            Toast.makeText(this, "본체 선택", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
            showRFIDDialog()
        }

        btnItem.setOnClickListener {
            // 물건 선택 후 RFID 인식 다이얼로그 표시
            Toast.makeText(this, "물건 선택", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
            showRFIDDialog()
        }

        dialog.show()
    }

    private fun showRFIDDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("RFID를 인식시켜주세요")
        builder.setMessage("RFID 리더기에 카드를 인식시켜주세요.")

        builder.setPositiveButton("확인") { dialog, _ ->
            dialog.dismiss()

            val invalidRfid = "RFID UID: 0 0 0 0 0 0 0 0 0 0 0 0 " // 인식되지 않은 RFID 값
            if (rfidData != null && rfidData != invalidRfid) {
                val storedRfid = rfidData
                Toast.makeText(this@MainActivity, "저장된 RFID: $storedRfid", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@MainActivity, "RFID 데이터가 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        rfidDialog = builder.create()
        rfidDialog?.show()

        // RFID 요청 메시지 전송
        webSocket.send("RFID 요청")
    }

    private fun showScheduleEditDialog() {
        val builder = AlertDialog.Builder(this)
        val inflater: LayoutInflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_schedule_edit, null)  // 스케줄 에딧 다이얼로그 레이아웃 사용
        builder.setView(dialogView)
        val dialog = builder.create()

        // 버튼 클릭 처리
        val btnCalendar: Button = dialogView.findViewById(R.id.button_calendar)
        val btnDay: Button = dialogView.findViewById(R.id.button_day)

        btnCalendar.setOnClickListener {
            Toast.makeText(this, "캘린더 선택", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        btnDay.setOnClickListener {
            val intent = Intent(this, WeekdayActivity::class.java)  // this 사용
            startActivity(intent)
            dialog.dismiss()
        }

        dialog.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        // 액티비티 종료 시 웹소켓 연결 닫기
        webSocket.close(1000, "앱 종료")
    }
}
