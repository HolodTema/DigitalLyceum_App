package com.terabyte.digitallyceum.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.terabyte.digitallyceum.RequestManager
import com.terabyte.digitallyceum.json.grades.GradeJson
import com.terabyte.digitallyceum.json.schools.SchoolJson

class ChooseGradeViewModel(application: Application, school: SchoolJson):
AndroidViewModel(application) {
    val liveDataGrades = MutableLiveData<List<GradeJson>?>()
    lateinit var chosenGrade: GradeJson

    init{
        RequestManager.getGradesForSchool(school.id) {
            liveDataGrades.value = it
        }
    }

    class Factory(private val application: Application, private val school: SchoolJson):
            ViewModelProvider.AndroidViewModelFactory(application) {

        override fun <T : ViewModel> create(modelClass: Class<T>)
            = ChooseGradeViewModel(application, school) as T
    }
}