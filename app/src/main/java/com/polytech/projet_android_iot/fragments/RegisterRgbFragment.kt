package com.polytech.projet_android_iot.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.polytech.projet_android_iot.R
import com.polytech.projet_android_iot.adapter.MyListAdapterPresets
import com.polytech.projet_android_iot.adapter.PresetsListener
import com.polytech.projet_android_iot.databinding.FragmentRegisterRgbBinding
import com.polytech.projet_android_iot.db.DatabaseIotUser
import com.polytech.projet_android_iot.viewmodel.RegisterRgbViewModel
import com.polytech.projet_android_iot.viewmodelfactory.RegisterRgbViewModelFactory

class RegisterRgbFragment : Fragment() {

    private lateinit var binding: FragmentRegisterRgbBinding
    private lateinit var viewModel: RegisterRgbViewModel
    private lateinit var viewModelFactory: RegisterRgbViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val application = requireNotNull(this.activity).application
        val dataSource = DatabaseIotUser.getInstance(application).userIOTDao
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register_rgb, container, false)
        val args = RegisterRgbFragmentArgs.fromBundle(requireArguments())
        val uid = args.uid
        val bid = args.bid
        val led1 = args.led1
        val led2 = args.led2
        val led3 = args.led3
        viewModelFactory = RegisterRgbViewModelFactory(dataSource,application,uid,bid,led1,led2,led3)
        viewModel = ViewModelProvider(this,viewModelFactory).get(RegisterRgbViewModel::class.java)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        val adapter = MyListAdapterPresets(PresetsListener {})

        binding.liPresetlist.adapter = adapter
        viewModel.presets.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        binding.apply {
            tvTitle.text = getString(R.string.registerRgbfrag)
            evRegisterName.hint = getString(R.string.presetname)
            btValidate.text = getString(R.string.validate)
        }

        viewModel.navigateToHomeFragment.observe(viewLifecycleOwner, { presetId ->
            presetId?.let {
                this.findNavController().navigate(
                    RegisterRgbFragmentDirections
                        .actionRegisterRgbFragmentToPersoMenuFragment(uid,bid))
                Log.i("Navigating to HOME", "Successful insert - presetId : $presetId")
                viewModel.doneNavigating()
            }
        })

        viewModel.errorRegistering.observe(viewLifecycleOwner, { errorCode ->
            errorCode?.let {
                var message = ""
                if(errorCode==1L){
                    message = "Name already exists in this board presets"
                }else if(errorCode==2L) {
                    message = "Name is empty"
                }
                Toast.makeText(this.context, message, Toast.LENGTH_SHORT).show()
            }
        })

        binding.btValidate.setOnClickListener {
            viewModel.onValidateName(binding.tiRegisterName.text.toString())
        }

        return binding.root
    }
}