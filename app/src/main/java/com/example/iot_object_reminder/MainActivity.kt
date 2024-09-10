package com.example.iot_object_reminder

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import okhttp3.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var webSocket: WebSocket

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 웹소켓 연결 설정
        initWebSocket()

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener {
            // 첫 번째 다이얼로그를 표시하는 함수 호출
            showCustomDialog()
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
                    Toast.makeText(this@MainActivity, "수신한 RFID: $text", Toast.LENGTH_SHORT).show()
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
        // 다이얼로그 빌더 생성
        val builder = AlertDialog.Builder(this)
        val inflater: LayoutInflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_register_item, null)
        builder.setView(dialogView)
        val dialog = builder.create()

        // 버튼 클릭 처리
        val btnBody: Button = dialogView.findViewById(R.id.btnBody)
        val btnItem: Button = dialogView.findViewById(R.id.btnItem)

        btnBody.setOnClickListener {
            // 본체 선택 후 RFID 인식 다이얼로그 표시
            dialog.dismiss() // 기존 다이얼로그 닫기
            showRFIDDialog() // RFID 다이얼로그 호출
        }

        btnItem.setOnClickListener {
            // 물건 선택 후 RFID 인식 다이얼로그 표시
            dialog.dismiss() // 기존 다이얼로그 닫기
            showRFIDDialog() // RFID 다이얼로그 호출
        }

        // 다이얼로그 표시
        dialog.show()
    }

    private fun showRFIDDialog() {
        // RFID 인식 다이얼로그 생성
        val builder = AlertDialog.Builder(this)
        builder.setTitle("RFID를 인식시켜주세요")
        builder.setMessage("RFID 리더기에 카드를 인식시켜주세요.")

        // 확인 버튼 추가
        builder.setPositiveButton("확인") { dialog, _ ->
            dialog.dismiss()
            // 웹소켓을 통해 RFID 데이터를 요청
            webSocket.send("RFID 요청")
        }

        // 다이얼로그 표시
        val rfidDialog = builder.create()
        rfidDialog.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        // 액티비티 종료 시 웹소켓 연결 닫기
        webSocket.close(1000, "앱 종료")
    }
}
