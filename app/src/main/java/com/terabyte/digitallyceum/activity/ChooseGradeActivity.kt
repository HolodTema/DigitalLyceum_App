package com.terabyte.digitallyceum.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.terabyte.digitallyceum.Const
import com.terabyte.digitallyceum.R
import com.terabyte.digitallyceum.databinding.ActivityChooseGradeOrSubgroupBinding
import com.terabyte.digitallyceum.databinding.RecyclerElementSubgroupsActiveBinding
import com.terabyte.digitallyceum.databinding.RecyclerElementSubgroupsInactiveBinding
import com.terabyte.digitallyceum.json.schools.SchoolJson
import com.terabyte.digitallyceum.json.subgroups.SubgroupJson
import com.terabyte.digitallyceum.viewmodel.ChooseGradeViewModel
import com.terabyte.digitallyceum.viewmodel.NoResponseViewModel

class ChooseGradeActivity : AppCompatActivity() {
    private lateinit var chosenSchool: SchoolJson

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_download)

        chosenSchool = intent.extras?.getParcelable(Const.INTENT_SCHOOL)!!

        val viewModel = ViewModelProvider(this, ChooseGradeViewModel.Factory(application, chosenSchool))[ChooseGradeViewModel::class.java]

        viewModel.liveDataGrades.observe(this) { grades ->
            if(grades==null || grades.isEmpty()) {
                val intent = Intent(this, NoResponseActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    .putExtra(Const.INTENT_NO_RESPONSE_TYPE, NoResponseViewModel.NoResponseType.Grades)
                    .putExtra(Const.INTENT_SCHOOL, chosenSchool)
                startActivity(intent)
            }
            else {
                val binding = ActivityChooseGradeOrSubgroupBinding.inflate(layoutInflater)
                setContentView(binding.root)

                val gradeNumbers = Array(grades.size){""}
                val gradeLetters = Array(grades.size){""}
                for((i, grade) in grades.withIndex()) {
                    gradeNumbers[i] = grade.number.toString()
                    gradeLetters[i]= grade.letter
                }

                binding.numberPickerGradeNumber.minValue = 0
                binding.numberPickerGradeNumber.maxValue = grades.size-1
                binding.numberPickerGradeNumber.displayedValues = gradeNumbers
                binding.numberPickerGradeNumber.value = viewModel.positionOfChosenGrade
                binding.numberPickerGradeNumber.setOnValueChangedListener { _, _, newValue ->
                    viewModel.positionOfChosenGrade = newValue
                    viewModel.chosenGrade = grades[newValue]
                    viewModel.updateSubgroups()
                    binding.recyclerSubgroups.visibility = View.GONE
                    binding.textNoSubgroupsForGrade.visibility = View.GONE
                    binding.progressBarSubgroups.visibility = View.VISIBLE
                    binding.buttonChoose.visibility = View.GONE
                }

                binding.numberPickerGradeLetter.minValue = 0
                binding.numberPickerGradeLetter.maxValue = grades.size-1
                binding.numberPickerGradeLetter.displayedValues = gradeLetters
                binding.numberPickerGradeLetter.value = viewModel.positionOfChosenGrade
                binding.numberPickerGradeLetter.setOnValueChangedListener { _, _, newValue ->
                    viewModel.positionOfChosenGrade = newValue
                    viewModel.chosenGrade = grades[newValue]
                    viewModel.updateSubgroups()
                    binding.recyclerSubgroups.visibility = View.GONE
                    binding.textNoSubgroupsForGrade.visibility = View.GONE
                    binding.progressBarSubgroups.visibility = View.VISIBLE
                    binding.buttonChoose.visibility = View.GONE
                }

                binding.recyclerSubgroups.visibility = View.GONE
                binding.textNoSubgroupsForGrade.visibility = View.GONE
                binding.progressBarSubgroups.visibility = View.VISIBLE
                binding.buttonChoose.visibility = View.GONE

                //the code below works when user clicks on "cancel" button
                //this case we need to start MainActivity (there will be schools list in MainActivity)
                binding.buttonCancel.setOnClickListener {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }

                viewModel.liveDataSubgroups.observe(this) { subgroups ->
                    if(subgroups!=null && subgroups.isNotEmpty()) {
                        val adapter = ChooseSubgroupAdapter(subgroups, viewModel, layoutInflater, resources.getString(R.string.subgroup_with_number))
                        binding.recyclerSubgroups.adapter = adapter
                        binding.recyclerSubgroups.layoutManager = LinearLayoutManager(this)

                        binding.recyclerSubgroups.visibility = View.VISIBLE
                        binding.progressBarSubgroups.visibility = View.GONE
                        binding.textNoSubgroupsForGrade.visibility = View.GONE
                        binding.buttonChoose.visibility = View.VISIBLE

                        binding.buttonChoose.setOnClickListener {
                            getSharedPreferences(Const.SH_PREFERENCES_NAME, MODE_PRIVATE)
                                .edit()
                                .putInt(Const.SH_PREF_KEY_SUBGROUP_ID, viewModel.chosenSubgroup.id)
                                .commit()

                            val intent = Intent(this, MainMenuActivity::class.java)
                                .putExtra(Const.INTENT_SCHOOL, chosenSchool)
                                .putExtra(Const.INTENT_GRADE, viewModel.chosenGrade)
                                .putExtra(Const.INTENT_SUBGROUP, viewModel.chosenSubgroup)
                            startActivity(intent)
                        }
                    }
                    else {
                        binding.recyclerSubgroups.visibility = View.GONE
                        binding.progressBarSubgroups.visibility = View.GONE
                        binding.textNoSubgroupsForGrade.visibility = View.VISIBLE
                        binding.buttonChoose.visibility = View.GONE
                    }
                }
            }
        }
    }

    //we need to save only chosenSchool field in onSaveInstanceState()
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable(Const.INTENT_SCHOOL, chosenSchool)
        super.onSaveInstanceState(outState)
    }



    class ChooseSubgroupAdapter(private val subgroups: List<SubgroupJson>, private val viewModel: ChooseGradeViewModel, private val inflater: LayoutInflater, private val strSubgroup: String): RecyclerView.Adapter<ChooseSubgroupAdapter.SubgroupHolder>() {

        override fun getItemViewType(position: Int): Int {
            return if(subgroups[position].id==viewModel.chosenSubgroup.id) ITEM_ACTIVE
            else ITEM_INACTIVE
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubgroupHolder {
            return when(viewType) {
                ITEM_ACTIVE -> SubgroupActiveHolder(RecyclerElementSubgroupsActiveBinding.inflate(inflater, parent, false))
                ITEM_INACTIVE -> SubgroupInactiveHolder(RecyclerElementSubgroupsInactiveBinding.inflate(inflater, parent, false))
                else -> SubgroupInactiveHolder(RecyclerElementSubgroupsInactiveBinding.inflate(inflater, parent, false))
            }
        }


        override fun onBindViewHolder(holder: SubgroupHolder, position: Int) {
            val subgroup = subgroups[position]

            holder.itemView.setOnClickListener{
                viewModel.chosenSubgroup = subgroup
                notifyDataSetChanged()
            }

            when(holder) {
                is SubgroupInactiveHolder -> {
                    holder.binding.textSubgroupName.text = subgroup.name
                    holder.binding.textSubgroupNumber.text = String.format(strSubgroup, position+1)

                }
                is SubgroupActiveHolder -> {
                    holder.binding.textSubgroupName.text = subgroup.name
                    holder.binding.textSubgroupNumber.text = String.format(strSubgroup, position+1)
                }
            }
        }

        override fun getItemCount() = subgroups.size

        companion object{
            const val ITEM_ACTIVE = 1
            const val ITEM_INACTIVE = 0
        }

        class SubgroupInactiveHolder(val binding: RecyclerElementSubgroupsInactiveBinding) : SubgroupHolder(binding.root)

        class SubgroupActiveHolder(val binding: RecyclerElementSubgroupsActiveBinding): SubgroupHolder(binding.root)

        abstract class SubgroupHolder(view: View): RecyclerView.ViewHolder(view)
    }
}