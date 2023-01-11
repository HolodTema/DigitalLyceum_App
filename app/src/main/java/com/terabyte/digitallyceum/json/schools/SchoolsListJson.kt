package com.terabyte.digitallyceum.json.schools

import com.squareup.moshi.Json

data class SchoolsListJson(
    @Json(name = "schools") val schools: List<SchoolJson>
)
