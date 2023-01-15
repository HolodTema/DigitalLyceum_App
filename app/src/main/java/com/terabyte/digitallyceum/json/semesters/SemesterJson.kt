package com.terabyte.digitallyceum.json.semesters

import com.squareup.moshi.Json

data class SemesterJson(
    @Json(name = "semester_id") val id: Int,
    @Json(name = "start_date") val startDate: DateJson,
    @Json(name = "end_date") val endDate: DateJson,
    @Json(name = "week_reverse") val weekReverse: Boolean,
    @Json(name = "school_id") val schoolId: Int
)
