package com.terabyte.digitallyceum.json.teachers

import com.squareup.moshi.Json

data class TeachersListJson(
    @Json(name = "teachers") val teachers: List<TeacherJson>
)
