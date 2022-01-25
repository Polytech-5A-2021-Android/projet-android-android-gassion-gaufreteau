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
import com.polytech.projet_android_iot.databinding.FragmentBoardconnectBinding
import com.polytech.projet_android_iot.db.DatabaseIotBoard
import com.polytech.projet_android_iot.db.DatabaseIotUser
import com.polytech.projet_android_iot.viewmodel.BoardConnectViewModel
import com.polytech.projet_android_iot.viewmodelfactory.BoardConnectViewModelFactory

class BoardConnectFragment : Fragment() {

    private lateinit var binding: FragmentBoardconnectBinding
    private lateinit var viewModel: BoardConnectViewModel
    private lateinit var viewModelFactory: BoardConnectViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val application = requireNotNull(this.activity).application
        val dataSource = DatabaseIotUser.getInstance(application).userIOTDao
        val dataSourceBoard = DatabaseIotBoard.getInstance(application).boardIOTDao
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_boardconnect, container, false)
        val args = BoardConnectFragmentArgs.fromBundle(requireArguments())
        val uid = args.uid
        viewModelFactory = BoardConnectViewModelFactory(dataSource,dataSourceBoard,application,uid)
        viewModel = ViewModelProvider(this,viewModelFactory).get(BoardConnectViewModel::class.java)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.apply {
            tvTitle.text = getString(R.string.connectBoard)
            evBoardCode.hint = getString(R.string.codeBoard)
            btSynchronize.text = getString(R.string.synchronize)
            tvConfirm.text = getString(R.string.enterConfirm)
            evConfirmCode.hint = getString(R.string.confirmationCode)
            btValidate.text = getString(R.string.validate)
        }

        viewModel.boardSynced.observe(viewLifecycleOwner, { ret ->
            ret?.let {
                //Change button Detected or not
                var message = ""
                if(ret) {
                    this.findNavController().navigate(
                        BoardConnectFragmentDirections
                            .actionBoardConnectFragmentToHomeFragment(uid))
                    Log.i("Navigating to HOME", "Successful sync - uid : $uid")
                    message = "Board synced"
                }else{
                    message = "Board not synced"
                }
                Toast.makeText(this.context, message, Toast.LENGTH_SHORT).show()
                viewModel.boardSyncDone()
            }
        })

        viewModel.boardDetected.observe(viewLifecycleOwner, { ret ->
            ret?.let {
                //Change button Detected or not
                var message = ""
                if(ret) {
                    message = "Board detected"
                }else{
                    message = "Board not detected"
                }
                viewModel._boardSyncMessage.value = message
                viewModel.boardDetectionDone()
            }
        })

        viewModel.errorRegistering.observe(viewLifecycleOwner, { errorCode ->
            errorCode?.let {
                var message = ""
                if(errorCode==1L){
                    message = "Confirmation code is invalid"
                }else if(errorCode==2L){
                    message = "Board code is not correct"
                }else if(errorCode==3L){
                    message = "This board already exists in the DB"
                }
                Toast.makeText(this.context, message, Toast.LENGTH_SHORT).show()
            }
        })

        binding.btValidate.setOnClickListener {
            viewModel.onValidateSync(binding.tiConfirmCode.text.toString())
        }


        return binding.root
    }
}