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
import com.polytech.projet_android_iot.databinding.FragmentPersoMatrixBinding
import com.polytech.projet_android_iot.db.DatabaseIotUser
import com.polytech.projet_android_iot.viewmodel.PersoMatrixViewModel
import com.polytech.projet_android_iot.viewmodelfactory.PersoMatrixViewModelFactory

class PersoMatrixFragment : Fragment() {

    private lateinit var binding: FragmentPersoMatrixBinding
    private lateinit var viewModel: PersoMatrixViewModel
    private lateinit var viewModelFactory: PersoMatrixViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        var soundDete = false
        val application = requireNotNull(this.activity).application
        val dataSource = DatabaseIotUser.getInstance(application).userIOTDao
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_perso_matrix, container, false)
        val args = PersoMatrixFragmentArgs.fromBundle(requireArguments())
        val uid = args.uid
        val bid = args.bid
        viewModelFactory = PersoMatrixViewModelFactory(dataSource,application,uid,bid)
        viewModel = ViewModelProvider(this,viewModelFactory)[PersoMatrixViewModel::class.java]

        binding.viewModel = viewModel

        binding.apply {
            tvTitle.text = getString(R.string.persomatrix)
            btDisplayEffect.text = getString(R.string.displayeffect)
            btMatrixMessage.text = getString(R.string.matrixmessage)
            btSoundDetector.text = getString(R.string.activateSound)
        }

        binding.btMatrixMessage.setOnClickListener {
            if(soundDete) {
                val message = "Sound detector is enabled"
                Toast.makeText(this.context, message, Toast.LENGTH_SHORT).show()
            }else {
                this.findNavController().navigate(
                    PersoMatrixFragmentDirections.actionPersoMatrixFragmentToMatrixMessageFragment(uid, bid)
                )
            }
        }

        binding.btSoundDetector.setOnClickListener {
            viewModel.switchSoundDetFromAPI(soundDete)
        }

        viewModel.res.observe(viewLifecycleOwner, { res ->
            res?.let {
                binding.btSoundDetector.text = if(soundDete) getString(R.string.activateSound) else getString(R.string.desactivateSound)
                soundDete = !soundDete
                var message = ""
                if(res) {

                    viewModel.doneNavigating()
                }else{
                    message = "Sound detector API call didn't work"
                }
                Toast.makeText(this.context, message, Toast.LENGTH_SHORT).show()
            }
        })

        return binding.root
    }
}