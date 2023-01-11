package com.terabyte.digitallyceum.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.terabyte.digitallyceum.R
import com.terabyte.digitallyceum.databinding.ItIsNotABugBinding
import com.terabyte.digitallyceum.viewmodel.MainMenuViewModel

class TeachersFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val viewModel: MainMenuViewModel by activityViewModels()
        val binding = ItIsNotABugBinding.inflate(layoutInflater)

        binding.buttonCancel.setOnClickListener {
            viewModel.updateChosenNavViewItemId(R.id.menuItemMain)
        }

        return binding.root
    }
}