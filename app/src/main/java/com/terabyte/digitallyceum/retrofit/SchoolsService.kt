package com.terabyte.digitallyceum.retrofit

import com.terabyte.digitallyceum.json.schools.SchoolsListJson
import retrofit2.Call
import retrofit2.http.GET

interface SchoolsService {
    @GET("api/schools")
    fun getSchools(): Call<SchoolsListJson>
}