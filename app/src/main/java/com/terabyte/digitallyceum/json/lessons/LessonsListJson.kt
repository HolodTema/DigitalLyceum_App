package com.terabyte.digitallyceum.json.lessons

import com.squareup.moshi.Json

data class LessonsListJson(
    @Json(name = "lessons") val lessons: List<LessonJson>
)
