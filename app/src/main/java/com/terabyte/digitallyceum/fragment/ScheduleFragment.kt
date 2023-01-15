package com.terabyte.digitallyceum.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.terabyte.digitallyceum.Const
import com.terabyte.digitallyceum.R
import com.terabyte.digitallyceum.RequestManager
import com.terabyte.digitallyceum.TabLayoutCanNotHaveMoreThan6TabsException
import com.terabyte.digitallyceum.activity.ChooseGradeActivity
import com.terabyte.digitallyceum.databinding.FragmentScheduleBinding
import com.terabyte.digitallyceum.json.lessons.LessonJson
import com.terabyte.digitallyceum.tabs.lessons.LessonsScheduleFragment
import com.terabyte.digitallyceum.viewmodel.MainMenuViewModel

class ScheduleFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentScheduleBinding.inflate(inflater, container, false)

        val viewModel: MainMenuViewModel by activityViewModels()

        val lessonsForDefiniteWeek = viewModel.getLessonsForDefiniteWeek(viewModel.chosenWeek)
        val adapter = LessonsTabsPagerAdapter(viewModel, childFragmentManager, lifecycle, lessonsForDefiniteWeek)
        binding.viewPagerSchedule.adapter = adapter
        binding.viewPagerSchedule.isUserInputEnabled = true
        TabLayoutMediator(binding.tabLayoutSchedule, binding.viewPagerSchedule) { tab, position ->
            tab.text = when(position) {
                0 -> resources.getString(R.string.monday)
                1 -> resources.getString(R.string.tuesday)
                2 -> resources.getString(R.string.wednesday)
                3 -> resources.getString(R.string.thursday)
                4 -> resources.getString(R.string.friday)
                5 -> resources.getString(R.string.saturday)
                else -> throw TabLayoutCanNotHaveMoreThan6TabsException()
            }
        }.attach()

        binding.textGradeName.text = String.format(resources.getString(R.string.grade_name), viewModel.grade)

        binding.toggleWeekType.visibility = View.GONE
        if(viewModel.areThere2WeekTypes) {
            binding.toggleWeekType.visibility = View.VISIBLE
            val strLowWeek = resources.getStringArray(R.array.weekTypes)[0]
            val strHighWeek = resources.getStringArray(R.array.weekTypes)[1]
            binding.toggleWeekType.isChecked = viewModel.chosenWeek
            binding.toggleWeekType.text = if(viewModel.chosenWeek) strHighWeek else strLowWeek
            binding.toggleWeekType.setOnCheckedChangeListener { _, b ->
                viewModel.chosenWeek = b
                binding.toggleWeekType.text = if(b) strHighWeek else strLowWeek
                viewModel.updateChosenNavViewItemId(R.id.menuItemSchedule)
            }
        }
        else binding.toggleWeekType.visibility = View.GONE

        binding.buttonChangeGrade.setOnClickListener {
            val shPreferences = requireActivity().getSharedPreferences(Const.SH_PREFERENCES_NAME, Context.MODE_PRIVATE)
            shPreferences.edit().remove(Const.SH_PREF_KEY_SUBGROUP_ID).commit()
            val intent = Intent(context, ChooseGradeActivity::class.java)
                .putExtra(Const.INTENT_SCHOOL, viewModel.school)
            startActivity(intent)
        }

        binding.buttonCancel.setOnClickListener {
            viewModel.updateChosenNavViewItemId(R.id.menuItemMain)
        }

        return binding.root
    }

    class LessonsTabsPagerAdapter(private var viewModel: MainMenuViewModel,
                                  fragmentManager: FragmentManager,
                                  lifecycle: Lifecycle,
                                  var lessons: List<LessonJson>): FragmentStateAdapter(fragmentManager, lifecycle){

        override fun getItemCount() = 6

        override fun createFragment(position: Int): Fragment {
            return when(position){
                0, 1, 2, 3, 4, 5 -> {
                    viewModel.updateLessonsForDefiniteDay(lessons, RequestManager.day0to6toCalendarFormat(position))
                    LessonsScheduleFragment()
                }
                else -> throw TabLayoutCanNotHaveMoreThan6TabsException()
            }
        }

    }

}