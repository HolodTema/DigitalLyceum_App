package com.terabyte.digitallyceum.json.lessons

import com.squareup.moshi.Json

data class LessonTimeInterval(
    @Json(name = "hour") val hour: Int,
    @Json(name = "minute") val minute: Int
)
