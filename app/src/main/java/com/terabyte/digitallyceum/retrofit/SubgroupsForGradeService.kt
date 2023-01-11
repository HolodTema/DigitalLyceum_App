package com.terabyte.digitallyceum.retrofit

import com.terabyte.digitallyceum.json.subgroups.SubgroupsListJson
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SubgroupsForGradeService {
    @GET("api/subgroups")
    fun getSubgroupsForGrade(@Query("class_id") gradeId: Int): Call<SubgroupsListJson>
}