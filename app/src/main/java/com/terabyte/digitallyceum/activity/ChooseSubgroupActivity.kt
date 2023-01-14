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
import com.terabyte.digitallyceum.databinding.ActivityChooseSubgroupBinding
import com.terabyte.digitallyceum.databinding.RecyclerElementSubgroupsBinding
import com.terabyte.digitallyceum.json.grades.GradeJson
import com.terabyte.digitallyceum.json.schools.SchoolJson
import com.terabyte.digitallyceum.json.subgroups.SubgroupJson
import com.terabyte.digitallyceum.viewmodel.ChooseSubgroupViewModel
import com.terabyte.digitallyceum.viewmodel.NoResponseViewModel

class ChooseSubgroupActivity : AppCompatActivity() {
    private lateinit var grade: GradeJson

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_download)

        grade = intent?.extras!!.getParcelable<GradeJson>(Const.INTENT_GRADE)!!

        val viewModel = ViewModelProvider(this, ChooseSubgroupViewModel.Factory(application, grade))[ChooseSubgroupViewModel::class.java]

        val school = intent?.extras?.getParcelable<SchoolJson>(Const.INTENT_SCHOOL)
        if(school!=null) viewModel.school = school

        viewModel.liveDataSubgroups.observe(this) {
            if(it==null || it.isEmpty()) {
                val intent = Intent(this, NoResponseActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    .putExtra(Const.INTENT_NO_RESPONSE_TYPE, NoResponseViewModel.NoResponseType.Subgroups)
                    .putExtra(Const.INTENT_SCHOOL, viewModel.school)
                    .putExtra(Const.INTENT_GRADE, grade)
                startActivity(intent)
            }
            else {
                val binding = ActivityChooseSubgroupBinding.inflate(layoutInflater)
                setContentView(binding.root)

                viewModel.chosenSubgroup = it[0]

                val adapter = ChooseSubgroupAdapter(this, viewModel.school, grade, it)
                binding.recyclerChooseSubgroup.adapter = adapter
                binding.recyclerChooseSubgroup.layoutManager = LinearLayoutManager(this)
                binding.recyclerChooseSubgroup.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

                binding.buttonCancel.setOnClickListener{
                    val intent = Intent(this, ChooseGradeActivity::class.java)
                        .putExtra(Const.INTENT_SCHOOL, viewModel.school)
                    startActivity(intent)
                }
            }
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable(Const.INTENT_GRADE, grade)
        super.onSaveInstanceState(outState)
    }

    class ChooseSubgroupAdapter(private val context: Context,
    private val chosenSchool: SchoolJson,
    private val chosenGrade: GradeJson,
    private val subgroups: List<SubgroupJson>): RecyclerView.Adapter<ChooseSubgroupAdapter.SubgroupJsonHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            SubgroupJsonHolder(RecyclerElementSubgroupsBinding.inflate(LayoutInflater.from(context), parent, false))

        override fun onBindViewHolder(holder: SubgroupJsonHolder, position: Int) {
            holder.bindingSubgroupElement.textSubgroupName.text = subgroups[position].name

            holder.bindingSubgroupElement.buttonChoose.setOnClickListener {
                context.getSharedPreferences(Const.SH_PREFERENCES_NAME, MODE_PRIVATE)
                    .edit()
                    .putInt(Const.SH_PREF_KEY_SUBGROUP_ID, subgroups[position].id)
                    .commit()

                val intent = Intent(context, MainMenuActivity::class.java)
                    .putExtra(Const.INTENT_SCHOOL, chosenSchool)
                    .putExtra(Const.INTENT_GRADE, chosenGrade)
                    .putExtra(Const.INTENT_SUBGROUP, subgroups[position])
                context.startActivity(intent)
            }
        }

        override fun getItemCount() = subgroups.size

        class SubgroupJsonHolder(val bindingSubgroupElement: RecyclerElementSubgroupsBinding): RecyclerView.ViewHolder(bindingSubgroupElement.root)
    }
}