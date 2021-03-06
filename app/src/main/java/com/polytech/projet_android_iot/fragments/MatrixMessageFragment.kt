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
import com.polytech.projet_android_iot.databinding.FragmentMatrixMessageBinding
import com.polytech.projet_android_iot.db.DatabaseIotUser
import com.polytech.projet_android_iot.viewmodel.MatrixMessageViewModel
import com.polytech.projet_android_iot.viewmodelfactory.MatrixMessageViewModelFactory


class MatrixMessageFragment : Fragment() {

    private lateinit var binding: FragmentMatrixMessageBinding
    private lateinit var viewModel: MatrixMessageViewModel
    private lateinit var viewModelFactory: MatrixMessageViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val application = requireNotNull(this.activity).application
        val dataSource = DatabaseIotUser.getInstance(application).userIOTDao
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_matrix_message, container, false)
        val args = MatrixMessageFragmentArgs.fromBundle(requireArguments())
        val uid = args.uid
        val bid = args.bid
        viewModelFactory = MatrixMessageViewModelFactory(dataSource,application,uid,bid)
        viewModel = ViewModelProvider(this,viewModelFactory)[MatrixMessageViewModel::class.java]

        binding.viewModel = viewModel

        binding.apply {
            tvTitle.text = getString(R.string.matrixmessage)
            tiMatrixmessage.hint = getString(R.string.matrixmessagehelp)
            btValidate.text = getString(R.string.validate)
        }

        viewModel.navigateToHomeFragment.observe(viewLifecycleOwner, { res ->
            res?.let {
                val message: String
                if(res) {
                    message = "Message printed"
                    this.findNavController().navigate(
                        MatrixMessageFragmentDirections.actionMatrixMessageFragmentToPersoMenuFragment(uid, bid)
                    )
                    Log.i("Navigating to HOME", "Successful message")
                    viewModel.doneNavigating()
                }else{
                    message = "Message not printed, API encountered an issue"
                }
                Toast.makeText(this.context, message, Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.errorRegistering.observe(viewLifecycleOwner, { errorCode ->
            errorCode?.let {
                var message = ""
                if(errorCode==1L){
                    message = "Message field is Empty"
                }
                Toast.makeText(this.context, message, Toast.LENGTH_SHORT).show()
            }
        })

        return binding.root
    }
}

