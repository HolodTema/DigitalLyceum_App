package com.terabyte.digitallyceum.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.terabyte.digitallyceum.Const
import com.terabyte.digitallyceum.RequestManager
import com.terabyte.digitallyceum.json.grades.GradeJson
import com.terabyte.digitallyceum.json.schools.SchoolJson
import com.terabyte.digitallyceum.json.subgroups.SubgroupJson

class MainViewModel(application: Application): AndroidViewModel(application) {
    val liveDataSchools = MutableLiveData<List<SchoolJson>>()
    val liveDataSchoolGradeSubgroupTrio = MutableLiveData<SchoolGradeSubgroupTrio>()

    init{
        val subgroupId = getSubgroupIdFromShPreferences()
        if(subgroupId==null) {
            //here we need to download schools, because user haven't chosen school yet.
            getSchools()
        }
        else {
            RequestManager.getDefineSubgroup(subgroupId) { subgroup ->
                if(subgroup==null) getSchools()
                else {
                    RequestManager.getDefineGrade(subgroup.gradeId) { grade ->
                        if(grade==null) getSchools()
                        else {
                            RequestManager.getDefineSchool(grade.schoolId) { school ->
                                if(school==null) getSchools()
                                else {
                                    liveDataSchoolGradeSubgroupTrio.value =
                                        SchoolGradeSubgroupTrio(school, grade, subgroup)
                                }
                            }
                        }
                    }
                }

            }
        }
    }

    private fun getSchools() {
        RequestManager.getSchools {
            liveDataSchools.value = it
        }
    }

    private fun getSubgroupIdFromShPreferences(): Int? {
        val subgroupId = getApplication<Application>()
            .getSharedPreferences(Const.SH_PREFERENCES_NAME, Context.MODE_PRIVATE)
            .getInt(Const.SH_PREF_KEY_SUBGROUP_ID, -1)
        return if(subgroupId==-1) null else subgroupId
    }

    class Factory(private val application: Application):
        ViewModelProvider.AndroidViewModelFactory(application) {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainViewModel(application) as T
        }
    }

    data class SchoolGradeSubgroupTrio(val school: SchoolJson,
    val grade: GradeJson, val subgroup: SubgroupJson)
}
