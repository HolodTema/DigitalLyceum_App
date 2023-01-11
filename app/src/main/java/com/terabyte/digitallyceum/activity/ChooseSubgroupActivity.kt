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
import com.terabyte.digitallyceum.databinding.RecyclerElementSubgroupsBinding
import com.terabyte.digitallyceum.json.grades.GradeJson
import com.terabyte.digitallyceum.json.schools.SchoolJson
import com.terabyte.digitallyceum.json.subgroups.SubgroupJson
import com.terabyte.digitallyceum.viewmodel.ChooseSubgroupViewModel
import com.terabyte.digitallyceum.viewmodel.NoResponseViewModel

class ChooseSubgroupActivity : AppCompatActivity() {
    private lateinit var grade: GradeJson
    private lateinit var viewModel: ChooseSubgroupViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_download)

        grade = intent?.extras!!.getParcelable<GradeJson>(Const.INTENT_GRADE)!!

        viewModel = ViewModelProvider(this, ChooseSubgroupViewModel.Factory(application, grade))[ChooseSubgroupViewModel::class.java]

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
                val binding = ActivityChooseGradeOrSubgroupBinding.inflate(layoutInflater)
                setContentView(binding.root)

                viewModel.chosenSubgroup = it[0]

                val adapter = ChooseSubgroupAdapter(it, layoutInflater, viewModel)
                binding.recyclerChooseGradeOrSubgroup.adapter = adapter
                binding.recyclerChooseGradeOrSubgroup.layoutManager = LinearLayoutManager(this)
                binding.recyclerChooseGradeOrSubgroup.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

                binding.buttonCancel.setOnClickListener{
                    val intent = Intent(this, ChooseGradeActivity::class.java)
                        .putExtra(Const.INTENT_SCHOOL, viewModel.school)

                    startActivity(intent)
                }

                binding.buttonNext.setOnClickListener {
                    getSharedPreferences(Const.SH_PREFERENCES_NAME, MODE_PRIVATE)
                        .edit()
                        .putInt(Const.SH_PREF_KEY_SUBGROUP_ID, viewModel.chosenSubgroup.id)
                        .commit()

                    val intent = Intent(this, MainMenuActivity::class.java)
                        .putExtra(Const.INTENT_SCHOOL, viewModel.school)
                        .putExtra(Const.INTENT_GRADE, grade)
                        .putExtra(Const.INTENT_SUBGROUP, viewModel.chosenSubgroup)
                    startActivity(intent)
                }
            }
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable(Const.INTENT_GRADE, grade)
        super.onSaveInstanceState(outState)
    }

    class ChooseSubgroupAdapter(
        private val subgroups: List<SubgroupJson>,
        private val inflater: LayoutInflater,
        private val viewModel: ChooseSubgroupViewModel):
        RecyclerView.Adapter<ChooseSubgroupAdapter.SubgroupJsonHolder>() {
        private var checkedRadioButton: CompoundButton? = null

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            SubgroupJsonHolder(RecyclerElementSubgroupsBinding.inflate(inflater))

        override fun onBindViewHolder(holder: SubgroupJsonHolder, position: Int) {
            holder.bindingSubgroupElement.textRecyclerElementSubgroupsName.text = subgroups[position].name

            if(subgroups[position].id == viewModel.chosenSubgroup.id) {
                holder.bindingSubgroupElement.radioButtonRecyclerElementSubgroup.isChecked = true
                checkedRadioButton = holder.bindingSubgroupElement.radioButtonRecyclerElementSubgroup
            }

            holder.bindingSubgroupElement.radioButtonRecyclerElementSubgroup.setOnCheckedChangeListener{ compoundButton, _ ->
                checkedRadioButton?.isChecked = false
                checkedRadioButton = compoundButton
                viewModel.chosenSubgroup = subgroups[position]
            }
        }

        override fun getItemCount() = subgroups.size

        class SubgroupJsonHolder(val bindingSubgroupElement: RecyclerElementSubgroupsBinding): RecyclerView.ViewHolder(bindingSubgroupElement.root)
    }
}