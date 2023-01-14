package com.terabyte.digitallyceum.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.terabyte.digitallyceum.Const
import com.terabyte.digitallyceum.activity.MainActivity
import com.terabyte.digitallyceum.databinding.DialogChangeSchoolAndGradeBinding

class ChangeSchoolAndGradeDialogFragment: DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)

        val inflater = requireActivity().layoutInflater
        val binding =  DialogChangeSchoolAndGradeBinding.inflate(inflater)
        binding.buttonNo.setOnClickListener {
            dismiss()
        }
        binding.buttonYes.setOnClickListener {
            requireActivity().getSharedPreferences(Const.SH_PREFERENCES_NAME, Context.MODE_PRIVATE)
                .edit()
                .remove(Const.SH_PREF_KEY_SUBGROUP_ID)
                .commit()
            startActivity(Intent(context, MainActivity::class.java))
        }
        builder.setView(binding.root)
        return builder.create()
    }
}