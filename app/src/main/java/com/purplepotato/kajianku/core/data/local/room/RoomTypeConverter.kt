package com.purplepotato.kajianku.core.data.local.room

import androidx.room.TypeConverter

class RoomTypeConverter {
    @TypeConverter
    fun getStringFromListOfString(list: List<String>): String {
        var string = ""

        for (i in list) {
            string += ",$i"
        }
        return string
    }

    @TypeConverter
    fun getListOfStringFromString(str: String): List<String> {
        val list = ArrayList<String>()

        val arrList = str.split(",")

        for (item in arrList) {
            list.add(item)
        }

        return list
    }
}