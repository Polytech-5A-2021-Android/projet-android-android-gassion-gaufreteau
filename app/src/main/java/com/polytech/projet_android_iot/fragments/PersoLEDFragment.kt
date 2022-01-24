package com.polytech.projet_android_iot.fragments

import android.os.Bundle
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
import com.polytech.projet_android_iot.databinding.FragmentPersoLEDBinding
import com.polytech.projet_android_iot.db.DatabaseIotUser
import com.polytech.projet_android_iot.viewmodel.ParamsViewModel
import com.polytech.projet_android_iot.viewmodel.PersoLEDViewModel
import com.polytech.projet_android_iot.viewmodelfactory.ParamsViewModelFactory
import com.polytech.projet_android_iot.viewmodelfactory.PersoLEDViewModelFactory


class PersoLEDFragment : Fragment() {

    private lateinit var binding: FragmentPersoLEDBinding
    private lateinit var viewModel: PersoLEDViewModel
    private lateinit var viewModelFactory: PersoLEDViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val application = requireNotNull(this.activity).application
        val dataSource = DatabaseIotUser.getInstance(application).userIOTDao
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_perso_l_e_d, container, false)
        val args = PersoLEDFragmentArgs.fromBundle(requireArguments())
        val uid = args.uid
        val bid = args.bid
        viewModelFactory = PersoLEDViewModelFactory(dataSource,application,uid)
        viewModel = ViewModelProvider(this,viewModelFactory).get(PersoLEDViewModel::class.java)

        binding.viewModel = viewModel

        binding.apply {
            tvTitle.text = getString(R.string.menuLED)
            btPreset.text = getString(R.string.presets)
            btColorPick.text = getString(R.string.colorPick)
        }

        binding.btPreset.setOnClickListener {
            this.findNavController().navigate(
                PersoLEDFragmentDirections.actionPersoLEDFragmentToPresetLEDFragment(uid,bid)
            )
        }

        binding.btColorPick.setOnClickListener {
            this.findNavController().navigate(
                PersoLEDFragmentDirections.actionPersoLEDFragmentToRGBPickerFragment(uid,bid)
            )
        }

        return binding.root
    }
}