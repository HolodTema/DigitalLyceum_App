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
import com.terabyte.digitallyceum.databinding.ActivityMainBinding
import com.terabyte.digitallyceum.databinding.RecyclerElementSchoolsBinding
import com.terabyte.digitallyceum.json.grades.GradeJson
import com.terabyte.digitallyceum.json.schools.SchoolJson
import com.terabyte.digitallyceum.json.subgroups.SubgroupJson
import com.terabyte.digitallyceum.viewmodel.MainViewModel
import com.terabyte.digitallyceum.viewmodel.NoResponseViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_download)

        viewModel = ViewModelProvider(this,
            MainViewModel.Factory(application)) [MainViewModel::class.java]

        viewModel.liveDataSchools.observe(this) { schools ->
            if(schools==null || schools.isEmpty()) {
                startNoResponseActivity()
            }
            else {
                val binding = ActivityMainBinding.inflate(layoutInflater)
                setContentView(binding.root)

                configRecyclerView(binding, schools)
            }
        }

        viewModel.liveDataSchoolGradeSubgroupTrio.observe(this) {
            startMainMenuActivity(it.school, it.grade, it.subgroup)
        }

    }

    private fun configRecyclerView(binding: ActivityMainBinding, schools: List<SchoolJson>) {
        val adapter = SchoolAdapter(this, schools)
        binding.recyclerChooseSchool.adapter = adapter
        binding.recyclerChooseSchool.layoutManager = LinearLayoutManager(this)
        binding.recyclerChooseSchool
            .addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
    }

    private fun startNoResponseActivity() {
        val intent = Intent(this, NoResponseActivity::class.java)
            .putExtra(Const.INTENT_NO_RESPONSE_TYPE, NoResponseViewModel.NoResponseType.Schools)
            .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        startActivity(intent)
    }

    private fun startMainMenuActivity(school: SchoolJson, grade: GradeJson, subgroup: SubgroupJson) {
        val intent = Intent(this, MainMenuActivity::class.java)
            .putExtra(Const.INTENT_SCHOOL, school)
            .putExtra(Const.INTENT_GRADE, grade)
            .putExtra(Const.INTENT_SUBGROUP, subgroup)
            .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        startActivity(intent)

    }

    class SchoolAdapter(private val context: Context, private val schools: List<SchoolJson>):
        RecyclerView.Adapter<SchoolAdapter.SchoolJsonHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            SchoolJsonHolder(RecyclerElementSchoolsBinding.inflate(LayoutInflater.from(parent.context), parent, false))

        override fun onBindViewHolder(holder: SchoolJsonHolder, position: Int) {
            //we set school name textView in every element of our RecyclerView
            holder.bindingSchoolElement.textSchoolName.text = schools[position].name
            holder.bindingSchoolElement.textSchoolAddress.text = schools[position].address

            holder.bindingSchoolElement.buttonChoose.setOnClickListener {
                val intent = Intent(context, ChooseGradeActivity::class.java)
                    .putExtra(Const.INTENT_SCHOOL, schools[position])
                context.startActivity(intent)
            }
        }

        override fun getItemCount() = schools.size

        //we also can use ViewBinding in viewHolder
        class SchoolJsonHolder(val bindingSchoolElement: RecyclerElementSchoolsBinding) : RecyclerView.ViewHolder(bindingSchoolElement.root)
    }
}