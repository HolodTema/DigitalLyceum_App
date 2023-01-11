package com.terabyte.digitallyceum.json.lessons

import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.Json
import com.terabyte.digitallyceum.json.teachers.TeacherJson

class LessonJson(
    @Json(name = "lesson_id") var id: Int,
    @Json(name = "name") var name: String,
    @Json(name = "start_time") val startTime: LessonTimeInterval,
    @Json(name = "end_time") val endTime: LessonTimeInterval,
    @Json(name = "week") var week: Boolean,
    @Json(name = "weekday") var weekday: Int,
    @Json(name = "room") var room: String,
    @Json(name = "teacher") var teacher: TeacherJson,
    @Json(name = "school_id") var schoolId: Int
): Parcelable, Comparable<LessonJson> {


    override fun compareTo(other: LessonJson): Int {
        return if(startTime.hour>other.startTime.hour) 1
        else if(startTime.hour==other.startTime.hour) {

            if(startTime.minute>other.startTime.minute) 1
            else if(startTime.minute==other.startTime.minute) 0
            else -1

        }
        else -1
    }

    override fun describeContents() = 0

    override fun writeToParcel(parcel: Parcel, p1: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeInt(startTime.hour)
        parcel.writeInt(startTime.minute)
        parcel.writeInt(endTime.hour)
        parcel.writeInt(endTime.minute)
        parcel.writeInt(if(week) 1 else 0)
        parcel.writeInt(weekday)
        parcel.writeString(room)
        parcel.writeInt(teacher.id)
        parcel.writeString(teacher.name)
        parcel.writeInt(schoolId)
    }

    companion object CREATOR: Parcelable.Creator<LessonJson> {
        override fun createFromParcel(parcel: Parcel?) =
            LessonJson(parcel!!.readInt(),
            parcel.readString()!!,
            LessonTimeInterval(parcel.readInt(), parcel.readInt()),
            LessonTimeInterval(parcel.readInt(), parcel.readInt()),
            parcel.readInt()==1,
            parcel.readInt(),
            parcel.readString()!!,
            TeacherJson(parcel.readInt(), parcel.readString()!!),
            parcel.readInt())

        override fun newArray(p0: Int) = arrayOfNulls<LessonJson>(p0)
    }
}