package com.terabyte.digitallyceum.viewmodel

import android.app.Application
import androidx.annotation.IdRes
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.terabyte.digitallyceum.R
import com.terabyte.digitallyceum.RequestManager
import com.terabyte.digitallyceum.json.grades.GradeJson
import com.terabyte.digitallyceum.json.lessons.LessonJson
import com.terabyte.digitallyceum.json.schools.SchoolJson
import com.terabyte.digitallyceum.json.subgroups.SubgroupJson
import java.util.*

class MainMenuViewModel(application: Application, val school: SchoolJson, val grade: GradeJson, val subgroup: SubgroupJson):
AndroidViewModel(application){
    val liveDataSchedule = MutableLiveData<List<LessonJson>>()
    val liveDataTodaySchedule = MutableLiveData<List<LessonJson>>()
    //we need this liveData for simple jumping between fragments in navView for proper working
    val liveDataChosenNavViewItemId = MutableLiveData<Int>()
    val liveDataLessonsForDefiniteDay = MutableLiveData<List<LessonJson>>()

    var chosenWeek = false
    var chosenDayCalendarFormat = Calendar.MONDAY

    var areThere2WeekTypes: Boolean = false

    init{
        RequestManager.getSchedule(subgroup.id, grade.id) { schedule ->
            liveDataSchedule.value = schedule
            if(schedule!=null) {
                areThere2WeekTypes = checkIfThereAre2WeekTypes(schedule)
                if(!areThere2WeekTypes) chosenWeek = RequestManager.getWhatBooleanValIsDefaultForOneWeek(schedule)
                updateChosenNavViewItemId(R.id.menuItemMain)
                RequestManager.getTodaySchedule(subgroup.id, grade.id) { todaySchedule ->
                    liveDataTodaySchedule.value = todaySchedule
                }
            }
        }
    }

    //we use this method in ScheduleFragment when the tab of tabLayout was changed and we need to get schedule for another day of week
    fun updateLessonsForDefiniteDay(lessons: List<LessonJson>, day: Int) {
        // TODO: we don't know what the week is here, so we use default week = 0
        liveDataLessonsForDefiniteDay.value = RequestManager.getLessonsForDefiniteDay(lessons, day)
    }

    fun getLessonsForDefiniteWeek(week: Boolean): List<LessonJson> {
        return RequestManager.getLessonsForDefiniteWeek(liveDataSchedule.value!!, week)
    }

    //this method allows to jump between fragments in navView
    fun updateChosenNavViewItemId(@IdRes itemId: Int) {
        liveDataChosenNavViewItemId.value = itemId
    }

    fun checkIfThereAre2WeekTypes(lessons: List<LessonJson>): Boolean {
        return RequestManager.checkIfThereAre2WeekTypes(lessons)
    }

    class Factory(private val application: Application, private val school: SchoolJson,
    private val grade: GradeJson, private val subgroup: SubgroupJson):
            ViewModelProvider.AndroidViewModelFactory(application) {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainMenuViewModel(application, school, grade, subgroup) as T
        }
    }
}