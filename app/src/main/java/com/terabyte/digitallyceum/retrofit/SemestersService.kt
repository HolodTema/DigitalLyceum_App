package com.terabyte.digitallyceum.retrofit

import com.terabyte.digitallyceum.json.semesters.SemestersListJson
import retrofit2.Call
import retrofit2.http.GET

interface SemestersService {
    @GET("api/semesters")
    fun getSemesters(): Call<SemestersListJson>
}