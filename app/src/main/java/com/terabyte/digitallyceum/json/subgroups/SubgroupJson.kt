package com.terabyte.digitallyceum.json.subgroups

import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.Json

data class SubgroupJson(
    @Json(name = "subgroup_id") val id: Int,
    @Json(name = "name") val name: String,
    @Json(name = "class_id") val gradeId: Int
): Parcelable, Comparable<SubgroupJson> {

    override fun describeContents() = 0

    override fun writeToParcel(parcel: Parcel, p1: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeInt(gradeId)
    }

    override fun compareTo(other: SubgroupJson): Int {
        return name.compareTo(other.name)
    }

    companion object CREATOR: Parcelable.Creator<SubgroupJson> {
        override fun createFromParcel(parcel: Parcel?): SubgroupJson {
            return SubgroupJson(parcel?.readInt()!!, parcel.readString()!!, parcel.readInt())
        }

        override fun newArray(p0: Int) = arrayOfNulls<SubgroupJson>(p0)
    }
}
