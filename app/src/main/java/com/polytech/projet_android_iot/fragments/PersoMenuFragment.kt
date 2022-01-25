package com.polytech.projet_android_iot.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.polytech.projet_android_iot.R
import com.polytech.projet_android_iot.databinding.FragmentPersoMenuBinding
import com.polytech.projet_android_iot.db.DatabaseIotUser
import com.polytech.projet_android_iot.viewmodel.PersoMenuViewModel
import com.polytech.projet_android_iot.viewmodelfactory.PersoMenuViewModelFactory

class PersoMenuFragment : Fragment() {

    private lateinit var binding: FragmentPersoMenuBinding
    private lateinit var viewModel: PersoMenuViewModel
    private lateinit var viewModelFactory: PersoMenuViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val application = requireNotNull(this.activity).application
        val dataSource = DatabaseIotUser.getInstance(application).userIOTDao
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_perso_menu, container, false)
        val args = PersoMenuFragmentArgs.fromBundle(requireArguments())
        val uid = args.uid
        val bid = args.bid
        viewModelFactory = PersoMenuViewModelFactory(dataSource,application,uid)
        viewModel = ViewModelProvider(this,viewModelFactory)[PersoMenuViewModel::class.java]

        binding.viewModel = viewModel

        binding.apply {
            tvTitle.text = getString(R.string.persomenu)
            btLedRGB.text = getString(R.string.ledrgb)
            btLedMatix.text = getString(R.string.ledmatrix)
        }

        binding.btLedRGB.setOnClickListener {
            this.findNavController().navigate(
                PersoMenuFragmentDirections.actionPersoMenuFragmentToPersoLEDFragment(uid,bid)
            )
        }

        binding.btLedMatix.setOnClickListener {
            this.findNavController().navigate(
                PersoMenuFragmentDirections.actionPersoMenuFragmentToPersoMatrixFragment(uid,bid)
            )
        }



        return binding.root
    }

}