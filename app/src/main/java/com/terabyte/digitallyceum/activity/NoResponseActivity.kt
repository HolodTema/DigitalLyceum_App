package com.terabyte.digitallyceum.activity

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.terabyte.digitallyceum.Const
import com.terabyte.digitallyceum.databinding.ActivityNoResponseBinding
import com.terabyte.digitallyceum.json.grades.GradeJson
import com.terabyte.digitallyceum.json.schools.SchoolJson
import com.terabyte.digitallyceum.json.subgroups.SubgroupJson
import com.terabyte.digitallyceum.viewmodel.NoResponseViewModel
import com.terabyte.digitallyceum.viewmodel.NoResponseViewModel.NoResponseType

class NoResponseActivity : AppCompatActivity() {
    private lateinit var noResponseType: NoResponseType

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityNoResponseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        noResponseType = intent.extras!!.getSerializable(Const.INTENT_NO_RESPONSE_TYPE)!!
                as NoResponseType

        val viewModel = ViewModelProvider(this, NoResponseViewModel.Factory(application, noResponseType)) [NoResponseViewModel::class.java]

        when(noResponseType) {
            NoResponseType.Schools -> {
                binding.buttonChooseAnotherSchool.visibility = View.GONE
            }
            NoResponseType.Grades -> {
                val chosenSchool = intent?.extras?.getParcelable<SchoolJson>(Const.INTENT_SCHOOL)
                if(chosenSchool!=null) viewModel.chosenSchool = chosenSchool
            }
            NoResponseType.Lessons -> {
                val chosenSchool = intent?.extras?.getParcelable<SchoolJson>(Const.INTENT_SCHOOL)
                if(chosenSchool!=null) viewModel.chosenSchool = chosenSchool
                val chosenGrade = intent?.extras?.getParcelable<GradeJson>(Const.INTENT_GRADE)
                if(chosenGrade!=null) viewModel.chosenGrade = chosenGrade
                val chosenSubgroup = intent?.extras?.getParcelable<SubgroupJson>(Const.INTENT_SUBGROUP)
                if(chosenSubgroup!=null) viewModel.chosenSubgroup = chosenSubgroup
            }
        }

        binding.buttonTryAgain.setOnClickListener {
            val intent = when(noResponseType) {
                NoResponseType.Schools -> {
                    Intent(this, MainActivity::class.java)
                }
                NoResponseType.Grades -> {
                    Intent(this, ChooseGradeActivity::class.java)
                        .putExtra(Const.INTENT_SCHOOL, viewModel.chosenSchool)
                }
                NoResponseType.Lessons -> {
                    Intent(this, MainMenuActivity::class.java)
                        .putExtra(Const.INTENT_SCHOOL, viewModel.chosenSchool)
                        .putExtra(Const.INTENT_GRADE, viewModel.chosenGrade)
                        .putExtra(Const.INTENT_SUBGROUP, viewModel.chosenSubgroup)
                }
            }
            startActivity(intent)
        }

        binding.buttonChooseAnotherSchool.setOnClickListener {
            if(noResponseType==NoResponseType.Lessons) {
                getSharedPreferences(Const.SH_PREFERENCES_NAME, MODE_PRIVATE)
                    .edit()
                    .remove(Const.SH_PREF_KEY_SUBGROUP_ID)
                    .commit()
            }
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        outState.putSerializable(Const.INTENT_NO_RESPONSE_TYPE, noResponseType)
        super.onSaveInstanceState(outState, outPersistentState)
    }


}