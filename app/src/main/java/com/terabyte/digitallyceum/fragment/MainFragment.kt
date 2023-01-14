package com.terabyte.digitallyceum.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.terabyte.digitallyceum.R
import com.terabyte.digitallyceum.RequestManager
import com.terabyte.digitallyceum.databinding.FragmentMainBinding
import com.terabyte.digitallyceum.databinding.LessonInScheduleInactiveBinding
import com.terabyte.digitallyceum.viewmodel.MainMenuViewModel
import java.util.*

class MainFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        //our viewModel instance by our MainMenuActivity
        //actually binding with 3 params including container and attachToParent is better, than simple 1-parameter method
        val viewModel: MainMenuViewModel by activityViewModels()
        val binding = FragmentMainBinding.inflate(inflater, container, false)

        binding.linearScheduleMain.visibility = View.GONE
        binding.textNoLessons.visibility = View.GONE
        binding.progressBarTodaySchedule.visibility = View.VISIBLE


        //the main fragment's idea is immutability of fragment like a finished ui component
        //we can't reset fragment's layout to another after onCreateView().
        //But we can have a case when we need to set layout according to data from viewModel, but there is observer, not straight
        //call in onCreateView - we can't call 'return binding.root' from observer.
        //the best solution is creating elements for every case. So, I created textNoLessons and linearScheduleMain for 2 different cases
        viewModel.liveDataTodaySchedule.observe(requireActivity()){ todaySchedule ->
            binding.progressBarTodaySchedule.visibility = View.GONE
            if(todaySchedule==null || todaySchedule.isEmpty()) {
                //there is no schedule for today from server.
                //maybe today is Sunday, but maybe there's an error on server...
                binding.textNoLessons.visibility = View.VISIBLE
                binding.linearScheduleMain.visibility = View.GONE
            }
            else {
                //we don't use recyclerView here, because we need to create scrolling ui in MainFragment for user.
                binding.textNoLessons.visibility = View.GONE
                binding.linearScheduleMain.visibility = View.VISIBLE

                // TODO: till new programming session there will be only inactive lessons
                var bindingLessonElement: LessonInScheduleInactiveBinding
                val strTime = resources.getString(R.string.lesson_time)
                for(lesson in todaySchedule) {
                    bindingLessonElement = LessonInScheduleInactiveBinding.inflate(layoutInflater)
                    bindingLessonElement.textLessonName.text = lesson.name
                    bindingLessonElement.textLessonTime.text = String.format(strTime, lesson.startTime.hour, lesson.startTime.minute, lesson.endTime.hour, lesson.endTime.minute)
                    binding.linearScheduleMain.addView(bindingLessonElement.root)
                }
            }
        }

        val calendar = Calendar.getInstance()
        val dayOfWeekCalendarFormat = calendar.get(Calendar.DAY_OF_WEEK)
        binding.textDayOfWeek.text = resources.getStringArray(R.array.daysOfWeek)[RequestManager.dayCalendarTo0to6Format(dayOfWeekCalendarFormat)]
        if(dayOfWeekCalendarFormat==Calendar.SUNDAY) {
            binding.textDayType.text = resources.getString(R.string.weekend)
            binding.imageDayType.setImageResource(R.drawable.day_type_weekend)
        }
        else {
            binding.textDayType.text = resources.getString(R.string.day_type_ordinary)
            binding.imageDayType.setImageResource(R.drawable.day_type_ordinary)
        }

        //there's button 'schedule' above linearScheduleMain
        binding.buttonSchedule.setOnClickListener {
            //updateChosenNavView is very cool method I've created for simple jumping between fragment in navView
            viewModel.updateChosenNavViewItemId(R.id.menuItemSchedule)
        }

        binding.buttonEvents.setOnClickListener {
            viewModel.updateChosenNavViewItemId(R.id.menuItemEvents)
        }

        return binding.root
    }

    private fun serverWeekDayNumberToStr(weekday: Int): String {
        //actually we have 3 systems of day's numbering:
        //1. 0..6 number segment like [monday; sunday]
        //2. response from server in weekday field. Now it's like 1st system,
        //but nobody knows what changes can happen, so it's different system
        //3. java.util.Calendar constants, for example Calendar.MONDAY, Calendar.THURSDAY etc.
        val daysStr = resources.getStringArray(R.array.daysOfWeek)
        return daysStr[weekday]
    }
}