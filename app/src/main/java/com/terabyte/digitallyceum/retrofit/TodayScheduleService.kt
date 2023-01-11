package com.terabyte.digitallyceum.retrofit

import com.terabyte.digitallyceum.json.lessons.LessonsListJson
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TodayScheduleService {
    @GET("api/lessons/today")
    fun getTodayScheduleForSubgroup(@Query("subgroup_id") subgroupId: Int, @Query("class_id") gradeId: Int): Call<LessonsListJson>
}