package com.terabyte.digitallyceum.retrofit

import com.terabyte.digitallyceum.json.grades.GradesListJson
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GradesForSchoolService {
    @GET("api/classes")
    fun getGradesForSchool(@Query("school_id") schoolId: Int): Call<GradesListJson>
}