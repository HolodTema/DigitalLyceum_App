package com.terabyte.digitallyceum.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.terabyte.digitallyceum.json.grades.GradeJson
import com.terabyte.digitallyceum.json.schools.SchoolJson
import com.terabyte.digitallyceum.json.subgroups.SubgroupJson

class NoResponseViewModel(application: Application, noResponseType: NoResponseType):
AndroidViewModel(application) {
    lateinit var chosenSchool: SchoolJson
    lateinit var chosenGrade: GradeJson
    lateinit var chosenSubgroup: SubgroupJson


    enum class NoResponseType {
        Schools, Grades, Lessons
    }

    class Factory(private val application: Application, private val noResponseType: NoResponseType):
    ViewModelProvider.AndroidViewModelFactory(application) {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return NoResponseViewModel(application, noResponseType) as T
        }
    }
}