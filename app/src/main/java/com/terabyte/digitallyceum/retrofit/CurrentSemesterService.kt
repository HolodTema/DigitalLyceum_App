package com.terabyte.digitallyceum.retrofit

import com.terabyte.digitallyceum.json.semesters.CurrentSemesterRootJson
import retrofit2.Call
import retrofit2.http.GET

interface CurrentSemesterService {
    @GET("api/semesters/current")
    fun getCurrentSemester(): Call<CurrentSemesterRootJson>
}