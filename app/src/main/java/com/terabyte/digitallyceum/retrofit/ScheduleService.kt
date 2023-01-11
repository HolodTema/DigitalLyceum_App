package com.terabyte.digitallyceum.retrofit

import com.terabyte.digitallyceum.json.lessons.LessonsListJson
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ScheduleService {
    @GET("api/lessons")
    fun getScheduleForSubgroup(@Query("subgroup_id") subgroupId: Int, @Query("class_id") gradeId: Int): Call<LessonsListJson>
}