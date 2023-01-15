package com.terabyte.digitallyceum.json.semesters

import com.squareup.moshi.Json

data class SemestersListJson(
    @Json(name = "semesters") val semesters: List<SemesterJson>
)
