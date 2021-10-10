package com.naman.news.DBroom

import androidx.room.TypeConverter
import com.naman.news.models.Source


class Converters {

    @TypeConverter
    fun fromSource(soruce:Source):String{
        return soruce.name
    }

    @TypeConverter
    fun toSorce(name:String):Source{
        return Source("name",name)
    }
}