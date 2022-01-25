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
import com.polytech.projet_android_iot.databinding.FragmentLoginBinding
import com.polytech.projet_android_iot.db.DatabaseIotUser
import com.polytech.projet_android_iot.viewmodel.LoginViewModel
import com.polytech.projet_android_iot.viewmodelfactory.LoginViewModelFactory


class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var viewModel: LoginViewModel
    private lateinit var viewModelFactory: LoginViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val application = requireNotNull(this.activity).application
        val dataSource = DatabaseIotUser.getInstance(application).userIOTDao
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        binding.lifecycleOwner = this

        viewModelFactory = LoginViewModelFactory(dataSource,application)
        viewModel = ViewModelProvider(this,viewModelFactory).get(LoginViewModel::class.java)

        binding.viewModel = viewModel

        binding.apply {
            tvTitle.text = getString(R.string.toconnect)
            evLogin.hint = getString(R.string.login)
            evPwd.hint = getString(R.string.password)
            btValidate.text = getString(R.string.validate)
            tvCreateacc.text = getString(R.string.createacc)
        }

        viewModel.navigateToHomeFragment.observe(viewLifecycleOwner, { uid ->
            uid?.let {
                this.findNavController().navigate(
                    LoginFragmentDirections
                        .actionLoginFragmentToHomeFragment(uid))
                Log.i("Navigating to HOME", "Successful login - uid : $uid")
                viewModel.doneNavigating()
            }
        })

        binding.tvCreateacc.setOnClickListener {
            this.findNavController().navigate(
                LoginFragmentDirections
                    .actionLoginFragmentToRegisterFragment()
            )
        }

        viewModel.errorLogin.observe(viewLifecycleOwner, { errorCode ->
            errorCode?.let {
                var message = ""
                if (errorCode) {
                    message = "User not recognized"
                }
                Toast.makeText(this.context, message, Toast.LENGTH_SHORT).show()
            }
        })

        return binding.root
    }


}