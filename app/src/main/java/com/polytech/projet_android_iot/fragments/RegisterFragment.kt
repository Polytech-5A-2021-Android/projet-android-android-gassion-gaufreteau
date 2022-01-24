package com.polytech.projet_android_iot.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.polytech.projet_android_iot.R
import com.polytech.projet_android_iot.databinding.FragmentRegisterBinding
import com.polytech.projet_android_iot.db.DatabaseIotUser
import com.polytech.projet_android_iot.viewmodel.RegisterViewModel
import com.polytech.projet_android_iot.viewmodelfactory.RegisterViewModelFactory
import java.util.*

class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private lateinit var viewModel: RegisterViewModel
    private lateinit var viewModelFactory: RegisterViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val application = requireNotNull(this.activity).application
        val dataSource = DatabaseIotUser.getInstance(application).userIOTDao
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false)

        viewModelFactory = RegisterViewModelFactory(dataSource,application)
        viewModel = ViewModelProvider(this,viewModelFactory).get(RegisterViewModel::class.java)

        binding.viewModel = viewModel

        binding.apply {
            tvTitle.text = getString(R.string.register)
            tiLogin.hint = getString(R.string.login)
            tiPwd.hint = getString(R.string.password)
            tiPwdCheck.hint = getString(R.string.passwordCheck)
            btValidate.text = getString(R.string.validate)
            rbMan.text = getString(R.string.man)
            rbWoman.text = getString(R.string.woman)
            tiAddress.hint = getString(R.string.address)
            tiFirstname.hint = getString(R.string.firstname)
            tiLastname.hint = getString(R.string.lastname)
            tiTown.hint = getString(R.string.town)
        }

        val spinner = binding.spCountries
        spinner.adapter = this.context?.let { ArrayAdapter<String>(it, android.R.layout.simple_dropdown_item_1line, viewModel.countries) }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                viewModel.getCountryPos(position)
            }
        }

        val datePicker = binding.dpDatePicker
        val cal = Calendar.getInstance()
        val y = cal.get(Calendar.YEAR)
        val m = cal.get(Calendar.MONTH)
        val d = cal.get(Calendar.DAY_OF_MONTH)
        datePicker.init(y,m,d) {
            view, year, month, day ->
            val month = month+1
            val date = "$day/$month/$year"
            val msg = "You Selected: $date"
            Toast.makeText(this.context, msg, Toast.LENGTH_SHORT).show()
            viewModel.setBday(date)
        }


        viewModel.navigateToHomeFragment.observe(viewLifecycleOwner, { uid ->
            uid?.let {
                this.findNavController().navigate(
                    RegisterFragmentDirections
                        .actionRegisterFragmentToHomeFragment(uid))
                Log.i("Navigating to HOME", "Successful registering - uid : $uid")
                viewModel.doneNavigating()
            }
        })

        viewModel.errorRegistering.observe(viewLifecycleOwner, { errorCode ->
            errorCode?.let {
                var message = ""
                when (errorCode) {
                    1L -> message = "Password and confirm don't match"
                    2L -> message = "You must select a gender"
                    3L -> message = "You must enter a lastname"
                    4L -> message = "You must enter a firstname"
                    5L -> message = "You must enter a town"
                    6L -> message = "You must enter an address"
                    7L -> message = "You must enter a password"
                    8L -> message = "You must enter a login"
                    9L -> message = "You must the confirmation of the password"
                    10L -> message = "You must select a country"
                    11L -> message = "You must select a date (Spin the picker !)"
                    12L -> message = "User Login already exists"
                }
                Toast.makeText(this.context, message, Toast.LENGTH_SHORT).show()
            }
        })


        return binding.root
    }


}