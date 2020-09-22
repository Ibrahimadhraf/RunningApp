package com.ibrahim.runningapp.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ibrahim.runningapp.R
import kotlinx.android.synthetic.main.setup_fragment.*

class SetUpFragments :Fragment(R.layout.setup_fragment) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvContinue.setOnClickListener {
            findNavController().navigate(R.id.action_setUpFragments_to_runFragments)
        }
    }
}