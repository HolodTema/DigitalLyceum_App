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

class ChooseSubgroupViewModel(application: Application, grade: GradeJson):
AndroidViewModel(application){
    lateinit var school: SchoolJson
    val liveDataSubgroups = MutableLiveData<List<SubgroupJson>>()
    lateinit var chosenSubgroup: SubgroupJson

    init {
        RequestManager.getSubgroupsForGrade(grade.id) {
            liveDataSubgroups.value = it
        }
    }

    class Factory(private val application: Application, private val grade: GradeJson):
            ViewModelProvider.AndroidViewModelFactory(application) {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ChooseSubgroupViewModel(application, grade) as T
        }
    }
}