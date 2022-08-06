package com.speculo.reservatio

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.speculo.reservatio.databinding.FragmentScanBinding

class ScanFragment : Fragment() {
    lateinit var binding: FragmentScanBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_scan, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        val inQueue = sharedPref.getBoolean("inQueue", false)

        if(inQueue){
            fragmentManager?.beginTransaction()?.replace(R.id.nav_host_fragment, QueueFragment())?.commit()
        }

        binding.apply {
            scanBTN.setOnClickListener{
                fragmentManager?.beginTransaction()?.replace(R.id.nav_host_fragment, CameraFragment())?.commit()
            }
        }
    }
}