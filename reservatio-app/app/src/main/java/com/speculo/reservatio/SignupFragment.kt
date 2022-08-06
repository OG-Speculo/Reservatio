package com.speculo.reservatio

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.speculo.reservatio.databinding.FragmentSignupBinding

class SignupFragment : Fragment() {
    lateinit var binding: FragmentSignupBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_signup, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        val flag = sharedPref.getString("name", null)
        if(flag != null){
            fragmentManager?.beginTransaction()?.replace(R.id.nav_host_fragment, ScanFragment())?.commit()
        }

        binding.apply {
            submitBTN.setOnClickListener {
                if(nameET.text.toString().trim() != ""){
                    with(sharedPref.edit()){
                        putString("name", nameET.text.toString())
                        apply()
                    }

                    fragmentManager?.beginTransaction()?.replace(R.id.nav_host_fragment, ScanFragment())?.commit()
                } else {
                    errorTV.visibility = View.VISIBLE
                }
            }
        }
    }
}