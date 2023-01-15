package com.terabyte.digitallyceum.retrofit

import android.util.Log
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.terabyte.digitallyceum.CantCreateRetrofitRequestException
import com.terabyte.digitallyceum.Const
import com.terabyte.digitallyceum.json.grades.GradeJson
import com.terabyte.digitallyceum.json.grades.GradesListJson
import com.terabyte.digitallyceum.json.lessons.LessonJson
import com.terabyte.digitallyceum.json.lessons.LessonsListJson
import com.terabyte.digitallyceum.json.schools.SchoolJson
import com.terabyte.digitallyceum.json.schools.SchoolsListJson
import com.terabyte.digitallyceum.json.semesters.CurrentSemesterRootJson
import com.terabyte.digitallyceum.json.semesters.SemesterJson
import com.terabyte.digitallyceum.json.semesters.SemestersListJson
import com.terabyte.digitallyceum.json.subgroups.SubgroupJson
import com.terabyte.digitallyceum.json.subgroups.SubgroupsListJson
import com.terabyte.digitallyceum.json.teachers.TeacherJson
import com.terabyte.digitallyceum.json.teachers.TeachersListJson
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitManager {
    private const val BASE_URL = "https://lava-land.ru"


    private var retrofit: Retrofit? = null

    private fun createClient() {
        if(retrofit==null) {
            val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(Const.NETWORK_CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .readTimeout(Const.NETWORK_READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .writeTimeout(Const.NETWORK_WRITE_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .build()

            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()))
                .build()
        }
    }

    fun getTeachers(listener: (List<TeacherJson>?) -> Unit) {
        createClient()
        val service = retrofit?.create(TeachersService::class.java)
        val call = service?.getTeachers() ?: throw CantCreateRetrofitRequestException()
        call.enqueue(object: Callback<TeachersListJson> {
            override fun onResponse(call: Call<TeachersListJson>, response: Response<TeachersListJson>) {
                listener(response.body()?.teachers)
            }

            override fun onFailure(call: Call<TeachersListJson>, t: Throwable) {
                Log.e(Const.LOG_TAG_RETROFIT_ON_FAILURE, t.toString())
                listener(null)
            }
        })
    }

    fun getSchools(listener: (List<SchoolJson>?) -> Unit) {
        createClient()
        val service = retrofit?.create(SchoolsService::class.java)
        val call = service?.getSchools() ?: throw CantCreateRetrofitRequestException()
        call.enqueue(object: Callback<SchoolsListJson>{
            override fun onResponse(call: Call<SchoolsListJson>, response: Response<SchoolsListJson>) {
                listener(response.body()?.schools)
            }
            override fun onFailure(call: Call<SchoolsListJson>, t: Throwable) {
                Log.e(Const.LOG_TAG_RETROFIT_ON_FAILURE, t.toString())
                listener(null)
            }
        })
    }

    fun getDefineSchool(schoolId: Int, listener: (SchoolJson?) -> Unit) {
        createClient()
        val service = retrofit?.create(DefineSchoolService::class.java)
        val call = service?.getDefineSchool(schoolId) ?: throw CantCreateRetrofitRequestException()
        call.enqueue(object: Callback<SchoolJson> {
            override fun onResponse(call: Call<SchoolJson>, response: Response<SchoolJson>) {
                listener(response.body())
            }

            override fun onFailure(call: Call<SchoolJson>, t: Throwable) {
                Log.e(Const.LOG_TAG_RETROFIT_ON_FAILURE, t.toString())
                listener(null)
            }
        })
    }

    fun getGradesForSchool(schoolId: Int, listener: (List<GradeJson>?) -> Unit) {
        createClient()
        val service = retrofit?.create(GradesForSchoolService::class.java)
        val call = service?.getGradesForSchool(schoolId) ?: throw CantCreateRetrofitRequestException()
        call.enqueue(object: Callback<GradesListJson>{
            override fun onResponse(call: Call<GradesListJson>, response: Response<GradesListJson>) {
                listener(response.body()?.schoolGrades)
            }

            override fun onFailure(call: Call<GradesListJson>, t: Throwable) {
                Log.e(Const.LOG_TAG_RETROFIT_ON_FAILURE, t.toString())
                listener(null)
            }

        })
    }

    fun getDefineGrade(gradeId: Int, listener: (GradeJson?) -> Unit) {
        createClient()
        val service = retrofit?.create(DefineGradeService::class.java)
        val call = service?.getDefineGrade(gradeId) ?: throw CantCreateRetrofitRequestException()
        call.enqueue(object: Callback<GradeJson> {
            override fun onResponse(call: Call<GradeJson>, response: Response<GradeJson>) {
                listener(response.body())
            }

            override fun onFailure(call: Call<GradeJson>, t: Throwable) {
                Log.e(Const.LOG_TAG_RETROFIT_ON_FAILURE, t.toString())
                listener(null)
            }
        })
    }

    fun getSubgroupsForGrade(gradeId: Int, listener: (List<SubgroupJson>?) -> Unit) {
        createClient()
        val service = retrofit?.create(SubgroupsForGradeService::class.java)
        val call = service?.getSubgroupsForGrade(gradeId) ?: throw CantCreateRetrofitRequestException()
        call.enqueue(object: Callback<SubgroupsListJson> {
            override fun onResponse(call: Call<SubgroupsListJson>, response: Response<SubgroupsListJson>) {
                listener(response.body()?.subgroups)
            }

            override fun onFailure(call: Call<SubgroupsListJson>, t: Throwable) {
                Log.e(Const.LOG_TAG_RETROFIT_ON_FAILURE, t.toString())
                listener(null)
            }
        })
    }

    fun getDefineSubgroup(subgroupId: Int, listener: (SubgroupJson?) -> Unit) {
        createClient()
        val service = retrofit?.create(DefineSubgroupService::class.java)
        val call = service?.getDefineSubgroup(subgroupId) ?: throw CantCreateRetrofitRequestException()
        call.enqueue(object: Callback<SubgroupJson> {
            override fun onResponse(call: Call<SubgroupJson>, response: Response<SubgroupJson>) {
                listener(response.body())
            }

            override fun onFailure(call: Call<SubgroupJson>, t: Throwable) {
                Log.e(Const.LOG_TAG_RETROFIT_ON_FAILURE, t.toString())
                listener(null)
            }
        })
    }

    fun getSchedule(subgroupId: Int, gradeId: Int, listener: (List<LessonJson>?) -> Unit) {
        createClient()
        val service = retrofit?.create(ScheduleService::class.java)
        val call = service?.getScheduleForSubgroup(subgroupId, gradeId) ?: throw CantCreateRetrofitRequestException()
        call.enqueue(object: Callback<LessonsListJson> {
            override fun onResponse(call: Call<LessonsListJson>, response: Response<LessonsListJson>) {
                listener(response.body()?.lessons)
            }

            override fun onFailure(call: Call<LessonsListJson>, t: Throwable) {
                Log.e(Const.LOG_TAG_RETROFIT_ON_FAILURE, t.toString())
                listener(null)
            }
        })
    }

    fun getTodaySchedule(subgroupId: Int, gradeId: Int, listener: (List<LessonJson>?) -> Unit) {
        createClient()
        val service = retrofit?.create(TodayScheduleService::class.java)
        val call = service?.getTodayScheduleForSubgroup(subgroupId, gradeId) ?: throw CantCreateRetrofitRequestException()
        call.enqueue(object: Callback<LessonsListJson> {
            override fun onResponse(call: Call<LessonsListJson>, response: Response<LessonsListJson>) {
                listener(response.body()?.lessons)
            }

            override fun onFailure(call: Call<LessonsListJson>, t: Throwable) {
                Log.e(Const.LOG_TAG_RETROFIT_ON_FAILURE, t.toString())
                listener(null)
            }
        })
    }

    fun getSemesters(listener: (List<SemesterJson>?) -> Unit) {
        createClient()
        val service = retrofit?.create(SemestersService::class.java)
        val call = service?.getSemesters() ?: throw CantCreateRetrofitRequestException()
        call.enqueue(object: Callback<SemestersListJson> {
            override fun onResponse(call: Call<SemestersListJson>, response: Response<SemestersListJson>) {
                listener(response.body()?.semesters)
            }

            override fun onFailure(call: Call<SemestersListJson>, t: Throwable) {
                Log.e(Const.LOG_TAG_RETROFIT_ON_FAILURE, t.toString())
                listener(null)
            }
        })
    }

    fun getCurrentSemester(listener: (CurrentSemesterRootJson?) -> Unit) {
        createClient()
        val service = retrofit?.create(CurrentSemesterService::class.java)
        val call = service?.getCurrentSemester() ?: throw CantCreateRetrofitRequestException()
        call.enqueue(object: Callback<CurrentSemesterRootJson> {
            override fun onResponse(call: Call<CurrentSemesterRootJson>, response: Response<CurrentSemesterRootJson>) {
                listener(response.body())
            }

            override fun onFailure(call: Call<CurrentSemesterRootJson>, t: Throwable) {
                Log.e(Const.LOG_TAG_RETROFIT_ON_FAILURE, t.toString())
                listener(null)
            }
        })
    }
}