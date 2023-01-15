package com.terabyte.digitallyceum.json.semesters

import com.squareup.moshi.Json

data class DateJson(
    @Json(name = "day") val day: Int,
    @Json(name = "month") val month: Int,
    @Json(name = "year") val year: Int
)
