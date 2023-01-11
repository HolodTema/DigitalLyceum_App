package com.terabyte.digitallyceum.retrofit


import com.terabyte.digitallyceum.json.subgroups.SubgroupJson
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface DefineSubgroupService {
    @GET("api/subgroups/{subgroup_id}")
    fun getDefineSubgroup(@Path("subgroup_id") subgroupId: Int): Call<SubgroupJson>
}