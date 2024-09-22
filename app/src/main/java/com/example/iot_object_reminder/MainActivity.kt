package com.example.iot_object_reminder

import android.os.Bundle
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class MainActivity : AppCompatActivity() {

    private lateinit var webSocketManager: WebSocketManager
    private lateinit var rfidDialogManager: RFIDDialogManager
    private lateinit var scheduleDialogManager: ScheduleDialogManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 웹소켓 매니저 초기화 및 연결 설정
        webSocketManager = WebSocketManager(this, webSocketListener)
        webSocketManager.initWebSocket("ws://192.168.4.1:8080")

        // RFIDDialogManager 초기화
        rfidDialogManager = RFIDDialogManager(this, webSocketManager)

        // ScheduleDialogManager 초기화
        scheduleDialogManager = ScheduleDialogManager(this)

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.imageTintList = ColorStateList.valueOf(Color.WHITE)
        fab.setOnClickListener {
            showCustomDialog() // Custom Dialog 표시
        }

        // 스케줄 편집 다이얼로그 호출
        val calendarIcon: ImageView = findViewById(R.id.calendar_icon)
        calendarIcon.setOnClickListener {
            scheduleDialogManager.showScheduleEditDialog()
        }

        // 앱 실행 시 저장된 RFID 정보를 확인
        val storedRfid = rfidDialogManager.getStoredRFID()
        if (storedRfid != null) {
            Toast.makeText(this, "저장된 RFID: $storedRfid", Toast.LENGTH_SHORT).show()
        }
    }

    private val webSocketListener = object : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {
            runOnUiThread {
                Toast.makeText(this@MainActivity, "웹소켓 연결 성공", Toast.LENGTH_SHORT).show()
            }
        }

        override fun onMessage(webSocket: WebSocket, text: String) { //웹소켓(WebSocket) 통신을 통해 서버로부터 받은 메시지를 처리
            runOnUiThread {
                rfidDialogManager.updateRFIDData(text)
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

    // Custom Dialog 관련 코드
    private fun showCustomDialog() {
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_register_item, null)
        builder.setView(dialogView)
        val dialog = builder.create()

        dialog.show()

        val btnBody: Button = dialogView.findViewById(R.id.btnBody)
        val btnItem: Button = dialogView.findViewById(R.id.btnItem)

        btnBody.setOnClickListener {
            Toast.makeText(this, "본체 선택", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
            rfidDialogManager.showRFIDDialog() // RFID Dialog 표시
            rfidDialogManager.rfidType = "본체"
        }

        btnItem.setOnClickListener {
            Toast.makeText(this, "물건 선택", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
            rfidDialogManager.showRFIDDialog() // RFID Dialog 표시
            rfidDialogManager.rfidType = "물건"
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // 액티비티 종료 시 웹소켓 연결 닫기
        webSocketManager.closeConnection()
    }
}
