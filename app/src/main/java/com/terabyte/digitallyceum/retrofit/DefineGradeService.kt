package com.terabyte.digitallyceum.retrofit

import com.terabyte.digitallyceum.json.grades.GradeJson
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface DefineGradeService {
    @GET("api/classes/{class_id}")
    fun getDefineGrade(@Path("class_id") gradeId: Int): Call<GradeJson>
}