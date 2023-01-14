package com.terabyte.digitallyceum.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.terabyte.digitallyceum.RequestManager
import com.terabyte.digitallyceum.json.grades.GradeJson
import com.terabyte.digitallyceum.json.schools.SchoolJson
import com.terabyte.digitallyceum.json.subgroups.SubgroupJson

class ChooseGradeViewModel(application: Application, school: SchoolJson):
AndroidViewModel(application) {
    val liveDataGrades = MutableLiveData<List<GradeJson>?>()
    val liveDataSubgroups = MutableLiveData<List<SubgroupJson>?>()
    lateinit var chosenGrade: GradeJson
    var positionOfChosenGrade = 0
    lateinit var chosenSubgroup: SubgroupJson

    init{
        RequestManager.getGradesForSchool(school.id) { grades ->
            if(grades!=null && grades.isNotEmpty()) chosenGrade = grades[0]
            liveDataGrades.value = grades
            if(grades!=null && grades.isNotEmpty()) {
                updateSubgroups()
            }
        }
    }

    fun updateSubgroups() {
        RequestManager.getSubgroupsForGrade(chosenGrade.id) { subgroups ->
            if(subgroups!=null && subgroups.isNotEmpty()) chosenSubgroup = subgroups[0]
            liveDataSubgroups.value = subgroups
        }
    }

    class Factory(private val application: Application, private val school: SchoolJson):
            ViewModelProvider.AndroidViewModelFactory(application) {

        override fun <T : ViewModel> create(modelClass: Class<T>)
            = ChooseGradeViewModel(application, school) as T
    }
}