package com.terabyte.digitallyceum.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.terabyte.digitallyceum.Const
import com.terabyte.digitallyceum.R
import com.terabyte.digitallyceum.databinding.ActivityChooseGradeBinding
import com.terabyte.digitallyceum.databinding.RecyclerElementGradesBinding
import com.terabyte.digitallyceum.json.grades.GradeJson
import com.terabyte.digitallyceum.json.schools.SchoolJson
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
                val binding = ActivityChooseGradeBinding.inflate(layoutInflater)
                setContentView(binding.root)

                val adapter = ChooseGradeAdapter(this, chosenSchool, grades)
                binding.recyclerChooseGrade.adapter = adapter
                binding.recyclerChooseGrade.layoutManager = LinearLayoutManager(this)
                binding.recyclerChooseGrade.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

                //the code below works when user clicks on "cancel" button
                //this case we need to start MainActivity (there will be schools list in MainActivity)
                binding.buttonCancel.setOnClickListener {
                    val intent = Intent(this, MainActivity::class.java)
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



    class ChooseGradeAdapter(private val context: Context, private val chosenSchool: SchoolJson, private val grades: List<GradeJson>): RecyclerView.Adapter<ChooseGradeAdapter.GradeJsonHolder>() {
        private val strClass = context.resources.getString(R.string.grade_name)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            GradeJsonHolder(RecyclerElementGradesBinding.inflate(LayoutInflater.from(parent.context), parent, false))


        override fun onBindViewHolder(holder: GradeJsonHolder, position: Int) {
            holder.bindingGradeElement.textGradeName.text = String.format(strClass, grades[position])

            holder.bindingGradeElement.buttonChoose.setOnClickListener {
                val intent = Intent(context, ChooseSubgroupActivity::class.java)
                    //we need to pass chosenGrade, chosenSchool and amountGrades into the ChooseSubgroupActivity
                    .putExtra(Const.INTENT_SCHOOL, chosenSchool)
                    .putExtra(Const.INTENT_GRADE, grades[position])
                context.startActivity(intent)
            }
        }

        override fun getItemCount() = grades.size

        class GradeJsonHolder(val bindingGradeElement: RecyclerElementGradesBinding) : RecyclerView.ViewHolder(bindingGradeElement.root)
    }
}