package com.example.iot_object_reminder

import android.content.Context
import android.content.SharedPreferences

class RFIDStorage(context: Context) {

    private val preferences: SharedPreferences = context.getSharedPreferences("RFID_PREFS", Context.MODE_PRIVATE)

    // RFID 데이터를 저장하는 함수
    fun saveRFID(rfid: String) {
        preferences.edit().putString("stored_rfid", rfid).apply()
    }

    // 저장된 RFID 데이터를 불러오는 함수
    fun getStoredRFID(): String? {
        return preferences.getString("stored_rfid", null)
    }

    // 저장된 RFID 데이터를 삭제하는 함수
    fun clearRFID() {
        preferences.edit().remove("stored_rfid").apply()
    }
}
