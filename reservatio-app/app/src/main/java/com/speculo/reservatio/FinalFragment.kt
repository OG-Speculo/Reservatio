package com.speculo.reservatio

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.speculo.reservatio.databinding.FragmentFinalBinding

class FinalFragment : Fragment() {
    lateinit var binding: FragmentFinalBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_final, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        val restaurant = sharedPref.getString("restaurant", null)
        Log.d("TAG", restaurant.toString())
        binding.apply {
            restaurantTV.text = restaurant

            homeBTN.setOnClickListener {
                fragmentManager?.beginTransaction()?.replace(R.id.nav_host_fragment, ScanFragment())?.commit()
            }
        }
        with(sharedPref.edit()){
            putString("restaurant", null)
            apply()
        }
    }
}