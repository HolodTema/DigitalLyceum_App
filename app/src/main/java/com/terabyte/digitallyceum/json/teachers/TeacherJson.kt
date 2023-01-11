package com.terabyte.digitallyceum.json.teachers

import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.Json


data class TeacherJson(
    @Json(name = "teacher_id") val id: Int,
    @Json(name = "name") val name: String
): Parcelable {
    override fun describeContents() = 0

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
    }

    companion object CREATOR: Parcelable.Creator<TeacherJson> {
        override fun createFromParcel(parcel: Parcel?) =
            TeacherJson(parcel?.readInt()!!, parcel.readString()!!)

        override fun newArray(p0: Int) = arrayOfNulls<TeacherJson>(p0)
    }
}
