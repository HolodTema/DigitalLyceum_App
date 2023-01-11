package com.terabyte.digitallyceum.retrofit

import com.terabyte.digitallyceum.json.teachers.TeachersListJson
import retrofit2.Call
import retrofit2.http.GET

interface TeachersService {
    @GET("api/teachers")
    fun getTeachers(): Call<TeachersListJson>
}