package com.example.iot_object_reminder

import android.content.Context
import android.content.SharedPreferences

class RFIDStorage(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("RFID_STORAGE", Context.MODE_PRIVATE)

    //  associated name 과 함께 RFID 데이터를 저장하는 함수
    fun saveRFID(uid: String, name: String, type: String) {
        val editor = sharedPreferences.edit()
        editor.putString("RFID_UID", uid)
        editor.putString("RFID_NAME", name)
        editor.putString("RFID_TYPE", type)
        editor.apply()
    }

    // 저장된 RFID 데이터를 불러오는 함수
    fun getStoredRFID(): String? {
        val uid = sharedPreferences.getString("RFID_UID", null)
        val name = sharedPreferences.getString("RFID_NAME", null)
        val type = sharedPreferences.getString("RFID_TYPE", null)
        return if (uid != null && name != null) {
            "UID: $uid, 이름: $name, 타입: $type"
        } else {
            null
        }
    }
/*
// 저장된 RFID 데이터를 삭제하는 함수
    fun clearRFID() {
        sharedPreferences.edit().remove("stored_rfid").apply()
    }*/
}
