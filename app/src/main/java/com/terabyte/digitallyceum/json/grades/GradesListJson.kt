package com.terabyte.digitallyceum.json.grades

import com.squareup.moshi.Json

data class GradesListJson(
    @Json(name = "classes") val schoolGrades: List<GradeJson>
)
