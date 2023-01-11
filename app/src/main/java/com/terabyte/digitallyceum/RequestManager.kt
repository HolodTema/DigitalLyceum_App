package com.terabyte.digitallyceum

import com.terabyte.digitallyceum.json.grades.GradeJson
import com.terabyte.digitallyceum.json.lessons.LessonJson
import com.terabyte.digitallyceum.json.schools.SchoolJson
import com.terabyte.digitallyceum.json.subgroups.SubgroupJson
import com.terabyte.digitallyceum.json.teachers.TeacherJson
import com.terabyte.digitallyceum.retrofit.RetrofitManager
import java.util.*

object RequestManager {
    fun getTeachers(listener: (List<TeacherJson>?) -> Unit) {
        RetrofitManager.getTeachers{
            listener(it)
        }
    }

    fun getSchools(listener: (List<SchoolJson>?) -> Unit) {
        RetrofitManager.getSchools{
            listener(it)
        }
    }

    fun getDefineSchool(schoolId: Int, listener: (SchoolJson?) -> Unit) {
        RetrofitManager.getDefineSchool(schoolId) {
            listener(it)
        }
    }

    fun getGradesForSchool(schoolId: Int, listener: (List<GradeJson>?) -> Unit) {
        RetrofitManager.getGradesForSchool(schoolId) {
            listener(it)
        }
    }

    fun getDefineGrade(gradeId: Int, listener: (GradeJson?) -> Unit) {
        RetrofitManager.getDefineGrade(gradeId) {
            listener(it)
        }
    }

    fun getSubgroupsForGrade(gradeId: Int, listener: (List<SubgroupJson>?) -> Unit) {
        RetrofitManager.getSubgroupsForGrade(gradeId) {
            listener(it)
        }
    }

    fun getDefineSubgroup(subgroupId: Int, listener: (SubgroupJson?) -> Unit) {
        RetrofitManager.getDefineSubgroup(subgroupId) {
            listener(it)
        }
    }

    fun getSchedule(subgroupId: Int, gradeId: Int, listener: (List<LessonJson>?) -> Unit) {
        RetrofitManager.getSchedule(subgroupId, gradeId) {
            listener(it)
        }
    }

    fun getTodaySchedule(subgroupId: Int, gradeId: Int, listener: (List<LessonJson>?) -> Unit) {
        RetrofitManager.getTodaySchedule(subgroupId, gradeId) {
            listener(it)
        }
    }

    fun getLessonsForDefiniteWeek(lessons: List<LessonJson>, week: Boolean): List<LessonJson> {
        val result = arrayListOf<LessonJson>()
        for(lesson in lessons) {
            if(lesson.week==week) result.add(lesson)
        }
        return result
    }

    fun getLessonsForDefiniteDay(lessons: List<LessonJson>, dayCalendarFormat: Int): List<LessonJson> {
        val day0to6 = dayCalendarTo0to6Format(dayCalendarFormat)
        val result = arrayListOf<LessonJson>()
        for(lesson in lessons) {
            if(lesson.weekday==day0to6) result.add(lesson)
        }
        return result
    }

    fun day0to6toCalendarFormat(day0to6: Int): Int {
        return when(day0to6) {
            0 -> Calendar.MONDAY
            1 -> Calendar.TUESDAY
            2 -> Calendar.WEDNESDAY
            3 -> Calendar.THURSDAY
            4 -> Calendar.FRIDAY
            5 -> Calendar.SATURDAY
            6 -> Calendar.SUNDAY
            else -> throw IncorrectDayOfWeekFormatException()
        }
    }

    fun dayCalendarTo0to6Format(dayCalendarFormat: Int): Int {
        return when(dayCalendarFormat) {
            Calendar.MONDAY -> 0
            Calendar.TUESDAY -> 1
            Calendar.WEDNESDAY -> 2
            Calendar.THURSDAY -> 3
            Calendar.FRIDAY -> 4
            Calendar.SATURDAY -> 5
            Calendar.SUNDAY -> 6
            else -> throw IncorrectDayOfWeekFormatException()
        }
    }

    fun checkIfThereAre2WeekTypes(lessons: List<LessonJson>): Boolean {
        var containLow = false
        var containHigh = false
        for(lesson in lessons) {
            if(lesson.week) containHigh = true
            else containLow = true
        }
        return containLow && containHigh
    }

    fun getWhatBooleanValIsDefaultForOneWeek(lessons: List<LessonJson>): Boolean {
        return lessons[0].week
    }
}