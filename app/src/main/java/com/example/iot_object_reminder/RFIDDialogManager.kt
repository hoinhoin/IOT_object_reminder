package com.example.iot_object_reminder

import android.content.Context
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.setPadding

class RFIDDialogManager(private val context: Context, private val webSocketManager: WebSocketManager) {

    private var rfidData: String? = null
    private var rfidDialog: AlertDialog? = null
    private val rfidStorage = RFIDStorage(context) // RFIDStorage 초기화

    fun showRFIDDialog() {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("RFID를 인식시켜주세요")

        // Create a layout to hold the TextView and EditText
        val layout = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50)  // Add padding if needed
        }

        // TextView to display the received RFID
        val rfidTextView = TextView(context).apply {
            id = R.id.rfid_text_view  // Set the ID for the TextView
            text = "수신된 RFID: " + (rfidData ?: "없음")
        }
        layout.addView(rfidTextView)

        // EditText for user to input RFID name
        val nameInput = EditText(context).apply {
            hint = "RFID 이름을 입력하세요"
        }
        layout.addView(nameInput)

        // Set the custom layout in the dialog
        builder.setView(layout)

        builder.setPositiveButton("확인") { dialog, _ ->
            dialog.dismiss()
            val invalidRfid = "0 0 0 0 0 0 0 0 0 0 0 0"
            val enteredName = nameInput.text.toString()

            if (rfidData != null && rfidData != invalidRfid) {
                val storedRfid = rfidData
                Toast.makeText(context, "저장된 RFID: $storedRfid", Toast.LENGTH_SHORT).show()

                // Save both RFID and name to local storage
                rfidStorage.saveRFID(storedRfid!!, enteredName)
            } else {
                Toast.makeText(context, "RFID 데이터가 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        rfidDialog = builder.create()
        rfidDialog?.show()

        // Send RFID request via WebSocket
        webSocketManager.sendMessage("RFID 요청")
    }

    // Update the received RFID data and reflect it in the dialog
    fun updateRFIDData(rfidData: String?) {
        this.rfidData = rfidData
        // Update the TextView in the dialog with the received RFID data
        rfidDialog?.findViewById<TextView>(R.id.rfid_text_view)?.text = "수신된 RFID: $rfidData"
    }

    // Get stored RFID
    fun getStoredRFID(): String? {
        return rfidStorage.getStoredRFID()
    }
}
