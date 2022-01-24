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
import com.polytech.projet_android_iot.databinding.FragmentPresetLEDBinding
import com.polytech.projet_android_iot.db.DatabaseIotUser
import com.polytech.projet_android_iot.viewmodel.PresetLEDViewModel
import com.polytech.projet_android_iot.viewmodelfactory.PresetLEDViewModelFactory

class PresetLEDFragment : Fragment() {

    private lateinit var binding: FragmentPresetLEDBinding
    private lateinit var viewModel: PresetLEDViewModel
    private lateinit var viewModelFactory: PresetLEDViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val application = requireNotNull(this.activity).application
        val dataSource = DatabaseIotUser.getInstance(application).userIOTDao
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_preset_l_e_d, container, false)
        val args = PresetLEDFragmentArgs.fromBundle(requireArguments())
        val uid = args.uid
        val bid = args.bid
        viewModelFactory = PresetLEDViewModelFactory(dataSource,application,uid,bid)
        viewModel = ViewModelProvider(this,viewModelFactory).get(PresetLEDViewModel::class.java)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val adapter = MyListAdapterPresets(PresetsListener { presetid ->
            Log.i("INFO -- Preset selected", "Preset choosen : $presetid")
            val res = viewModel.usePresetFromAPI(presetid)
            if(res) {
                this.findNavController().navigate(
                    PresetLEDFragmentDirections.actionPresetLEDFragmentToPersoMenuFragment(uid, bid)
                )
            }else{
                val message = "Problem with selection of the preset"
                Toast.makeText(this.context, message, Toast.LENGTH_SHORT).show()
            }
        })

        binding.liPresetlist.adapter = adapter
        viewModel.presets.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        binding.apply {
            tvTitle.text = getString(R.string.presetsList)
        }

        return binding.root
    }
}