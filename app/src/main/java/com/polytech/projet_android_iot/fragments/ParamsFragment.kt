package com.polytech.projet_android_iot.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.polytech.projet_android_iot.R
import com.polytech.projet_android_iot.databinding.FragmentParamsBinding
import com.polytech.projet_android_iot.db.DatabaseIotUser
import com.polytech.projet_android_iot.viewmodel.ParamsViewModel
import com.polytech.projet_android_iot.viewmodelfactory.ParamsViewModelFactory

class ParamsFragment : Fragment() {

    private lateinit var binding: FragmentParamsBinding
    private lateinit var viewModel: ParamsViewModel
    private lateinit var viewModelFactory: ParamsViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val application = requireNotNull(this.activity).application
        val dataSource = DatabaseIotUser.getInstance(application).userIOTDao
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_params, container, false)

        val args = ParamsFragmentArgs.fromBundle(requireArguments())
        val uid = args.uid

        viewModelFactory = ParamsViewModelFactory(dataSource,application,uid)
        viewModel = ViewModelProvider(this,viewModelFactory)[ParamsViewModel::class.java]

        binding.viewModel = viewModel

        binding.apply {
            tvTitle.text = getString(R.string.params)
            evOldPwd.hint = getString(R.string.oldpwd)
            evNewPwd.hint = getString(R.string.newpwd)
            evNewPwdCheck.hint = getString(R.string.newpwdcheck)
            btValidate.text = getString(R.string.validate)
        }


        viewModel.navigateToHomeFragment.observe(viewLifecycleOwner, { uid ->
            uid?.let {
                this.findNavController().navigate(
                    ParamsFragmentDirections
                        .actionParamsFragmentToHomeFragment(uid))
                Log.i("Navigating to HOME", "Successful update - uid : $uid")
                viewModel.doneNavigating()
            }
        })

        viewModel.errorRegistering.observe(viewLifecycleOwner, { errorCode ->
            errorCode?.let {
                var message = ""
                if(errorCode==1L){
                    message = "New password and confirm don't match"
                }else if(errorCode==2L){
                    message = "Old password is incorrect"
                }
                Toast.makeText(this.context, message, Toast.LENGTH_SHORT).show()
            }
        })

        return binding.root
    }
}