package com.example.iot_object_reminder

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

class RFIDDialogManager(private val context: Context, private val webSocketManager: WebSocketManager) {

    private var rfidData: String? = null // 수신한 RFID 데이터를 저장할 변수
    private var rfidDialog: AlertDialog? = null // RFID 다이얼로그를 저장할 변수

    fun showRFIDDialog() {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("RFID를 인식시켜주세요")
        builder.setMessage("RFID 리더기에 카드를 인식시켜주세요.")

        builder.setPositiveButton("확인") { dialog, _ ->
            dialog.dismiss()
            val invalidRfid = "RFID UID: 0 0 0 0 0 0 0 0 0 0 0 0 " // 인식되지 않은 RFID 값
            if (rfidData != null && rfidData != invalidRfid) {
                val storedRfid = rfidData
                Toast.makeText(context, "저장된 RFID: $storedRfid", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "RFID 데이터가 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        rfidDialog = builder.create()
        rfidDialog?.show()

        // RFID 요청 메시지 전송
        webSocketManager.sendMessage("RFID 요청")
    }

    fun updateRFIDData(rfidData: String?) {
        this.rfidData = rfidData
        rfidDialog?.setMessage("수신된 RFID: $rfidData")
    }
}
