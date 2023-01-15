package com.terabyte.digitallyceum.json.semesters

import com.squareup.moshi.Json

data class CurrentSemesterRootJson(
    @Json(name = "week") val week: Boolean,
    @Json(name = "semester") val semester: SemesterJson
)
