package com.terabyte.digitallyceum.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.terabyte.digitallyceum.NoSuchGradeException
import com.terabyte.digitallyceum.RequestManager
import com.terabyte.digitallyceum.json.grades.GradeJson
import com.terabyte.digitallyceum.json.schools.SchoolJson
import com.terabyte.digitallyceum.json.subgroups.SubgroupJson

class ChooseGradeViewModel(application: Application, school: SchoolJson):
AndroidViewModel(application) {
    val liveDataGrades = MutableLiveData<List<GradeJson>?>()
    val liveDataSubgroups = MutableLiveData<List<SubgroupJson>?>()
    lateinit var chosenGrade: GradeJson
    var positionOfChosenGradeNumber = 0
    var positionOfChosenGradeLetter = 0
    val hashMapGradeNumbersAndLetters = hashMapOf<String, HashSet<String>>()
    lateinit var chosenSubgroup: SubgroupJson

    init{
        RequestManager.getGradesForSchool(school.id) { grades ->
            if(grades!=null && grades.isNotEmpty()) {
                for(grade in grades) {
                    if(!hashMapGradeNumbersAndLetters.containsKey(grade.number.toString())) {
                        hashMapGradeNumbersAndLetters[grade.number.toString()] = hashSetOf(grade.letter)
                    }
                    else {
                        hashMapGradeNumbersAndLetters.getValue(grade.number.toString()).add(grade.letter)
                    }
                }
                chosenGrade = getGradeByNumberAndLetterPosition(grades)
            }
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

    //this method should be called only after all grades are gotten.
    fun getGradeByNumberAndLetterPosition(grades: List<GradeJson>): GradeJson {
        val chosenKey = hashMapGradeNumbersAndLetters.keys.toTypedArray()[positionOfChosenGradeNumber]
        val chosenValue = hashMapGradeNumbersAndLetters.getValue(chosenKey).toTypedArray()[positionOfChosenGradeLetter]
        for(grade in grades) {
            if(grade.number.toString()==chosenKey && grade.letter==chosenValue) {
                return grade
            }
        }
        throw NoSuchGradeException()
    }

    class Factory(private val application: Application, private val school: SchoolJson):
            ViewModelProvider.AndroidViewModelFactory(application) {

        override fun <T : ViewModel> create(modelClass: Class<T>)
            = ChooseGradeViewModel(application, school) as T
    }
}