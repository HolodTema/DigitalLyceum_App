package com.terabyte.digitallyceum.json.subgroups

import com.squareup.moshi.Json

data class SubgroupsListJson(
    @Json(name = "subgroups") val subgroups: List<SubgroupJson>
)
