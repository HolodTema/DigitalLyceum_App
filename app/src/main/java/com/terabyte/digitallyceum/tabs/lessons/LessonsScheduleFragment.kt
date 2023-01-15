package com.terabyte.digitallyceum.tabs.lessons

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.terabyte.digitallyceum.NoLessonsWhenFragmentCreatedException
import com.terabyte.digitallyceum.R
import com.terabyte.digitallyceum.databinding.FragmentLessonsScheduleBinding
import com.terabyte.digitallyceum.databinding.FragmentNoLessonsScheduleBinding
import com.terabyte.digitallyceum.databinding.LessonInScheduleInactiveBinding
import com.terabyte.digitallyceum.json.lessons.LessonJson
import com.terabyte.digitallyceum.viewmodel.MainMenuViewModel

class LessonsScheduleFragment : Fragment() {
    private val viewModel: MainMenuViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val lessons = viewModel.liveDataLessonsForDefiniteDay.value

        if(lessons!=null) {
            return if(lessons.isEmpty()) FragmentNoLessonsScheduleBinding.inflate(layoutInflater).root
            else {
                val binding = FragmentLessonsScheduleBinding.inflate(layoutInflater)
                val adapter = LessonsAdapter(layoutInflater, lessons, resources.getString(R.string.lesson_time))
                binding.recyclerLessons.adapter = adapter
                binding.recyclerLessons.layoutManager = LinearLayoutManager(activity)
                binding.root
            }
        }
        else throw NoLessonsWhenFragmentCreatedException()
    }

    class LessonsAdapter(private val inflater: LayoutInflater, private val lessons: List<LessonJson>, private val strTime: String): RecyclerView.Adapter<LessonsAdapter.LessonHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = LessonHolder(
            LessonInScheduleInactiveBinding.inflate(inflater, parent, false))

        override fun onBindViewHolder(holder: LessonHolder, position: Int) {
            val lesson = lessons[position]
            holder.binding.textLessonName.text = lesson.name
            holder.binding.textLessonTime.text = String.format(strTime, lesson.startTime.hour, lesson.startTime.minute, lesson.endTime.hour, lesson.endTime.minute)
            holder.binding.textLessonChamber.text = lesson.room
            holder.binding.textLessonTeacher.text = lesson.teacher.name
        }

        override fun getItemCount() = lessons.size

        class LessonHolder(val binding: LessonInScheduleInactiveBinding) : RecyclerView.ViewHolder(binding.root)
    }
}