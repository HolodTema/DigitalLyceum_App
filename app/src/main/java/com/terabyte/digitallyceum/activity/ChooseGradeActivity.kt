package com.terabyte.digitallyceum.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.terabyte.digitallyceum.Const
import com.terabyte.digitallyceum.R
import com.terabyte.digitallyceum.databinding.ActivityChooseGradeOrSubgroupBinding
import com.terabyte.digitallyceum.databinding.RecyclerElementGradesBinding
import com.terabyte.digitallyceum.json.grades.GradeJson
import com.terabyte.digitallyceum.json.schools.SchoolJson
import com.terabyte.digitallyceum.viewmodel.ChooseGradeViewModel
import com.terabyte.digitallyceum.viewmodel.NoResponseViewModel

class ChooseGradeActivity : AppCompatActivity() {
    private lateinit var viewModel: ChooseGradeViewModel
    private lateinit var chosenSchool: SchoolJson
    private lateinit var adapter: ChooseGradeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_download)

        chosenSchool = intent.extras?.getParcelable(Const.INTENT_SCHOOL)!!

        viewModel = ViewModelProvider(this, ChooseGradeViewModel.Factory(application, chosenSchool))[ChooseGradeViewModel::class.java]

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

                viewModel.chosenGrade = grades[0]

                adapter = ChooseGradeAdapter(grades, viewModel)
                binding.recyclerChooseGradeOrSubgroup.adapter = adapter

                binding.recyclerChooseGradeOrSubgroup.layoutManager = LinearLayoutManager(this)
                binding.recyclerChooseGradeOrSubgroup.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

                //and the code below works when user clicks on "Next" button
                binding.buttonNext.setOnClickListener {
                    //here we need to download subgroups for the chosenGrade
                    val intent = Intent(this, ChooseSubgroupActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    //we need to pass chosenGrade, chosenSchool and amountGrades into the ChooseSubgroupActivity
                        .putExtra(Const.INTENT_SCHOOL, chosenSchool)
                        .putExtra(Const.INTENT_GRADE, viewModel.chosenGrade)
                        .putExtra(Const.INTENT_AMOUNT_GRADES, viewModel.liveDataGrades.value?.size)
                    startActivity(intent)
                }

                //the code below works when user clicks on "cancel" button
                //this case we need to start MainActivity (there will be schools list in MainActivity)
                binding.buttonCancel.setOnClickListener {
                    val intent = Intent(this, MainActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    startActivity(intent)
                }
            }
        }
    }

    //we need to save only chosenSchool field in onSaveInstanceState()
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable(Const.INTENT_SCHOOL, chosenSchool)
        super.onSaveInstanceState(outState)
    }



    class ChooseGradeAdapter(private val grades: List<GradeJson>, private val viewModel: ChooseGradeViewModel): RecyclerView.Adapter<ChooseGradeAdapter.GradeJsonHolder>() {
        private var checkedRadioButton: CompoundButton? = null

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            GradeJsonHolder(RecyclerElementGradesBinding.inflate(LayoutInflater.from(parent.context), parent, false))


        override fun onBindViewHolder(holder: GradeJsonHolder, position: Int) {
            holder.bindingGradeElement.textRecyclerElementGradesName.text = grades[position].toString()

            if(grades[position].id == viewModel.chosenGrade.id) {
                holder.bindingGradeElement.radioButtonRecyclerElementGrade.isChecked = true
                checkedRadioButton = holder.bindingGradeElement.radioButtonRecyclerElementGrade

            }

            holder.bindingGradeElement.radioButtonRecyclerElementGrade.setOnCheckedChangeListener { compoundButton, _ ->
                checkedRadioButton?.isChecked = false
                checkedRadioButton = compoundButton
                viewModel.chosenGrade = grades[position]
            }
        }

        override fun getItemCount() = grades.size

        class GradeJsonHolder(val bindingGradeElement: RecyclerElementGradesBinding) : RecyclerView.ViewHolder(bindingGradeElement.root)
    }
}