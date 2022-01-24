package com.polytech.projet_android_iot.fragments.exemples

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.polytech.projet_android_iot.LongConverter
import com.polytech.projet_android_iot.R
import com.polytech.projet_android_iot.databinding.FragmentPersonalDataBinding
import com.polytech.projet_android_iot.db.DBase
import com.polytech.projet_android_iot.viewmodel.exemples.PersonalDataViewModel
import com.polytech.projet_android_iot.viewmodelfactory.exemples.PersonalDataViewModelFactory

class PersonalDataFragment : Fragment() { //, PersonalDateEventListener {
    private lateinit var binding: FragmentPersonalDataBinding
    private lateinit var viewModel: PersonalDataViewModel
    private lateinit var viewModelFactory: PersonalDataViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val application = requireNotNull(this.activity).application
        val dataSource = DBase.getInstance(application).userDao
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_personal_data, container, false)
//        binding.eventListener = this
        binding.lifecycleOwner = this
        val args = PersonalDataFragmentArgs.fromBundle(requireArguments())
        val viewModelFactory = PersonalDataViewModelFactory(args.user,dataSource, application,args.user.id)
        viewModel = ViewModelProvider(this,viewModelFactory).get(PersonalDataViewModel::class.java)

        //viewModel = ViewModelProviders.of(this, viewModelFactory)
        //    .get(PersonalDataViewModel::class.java)
        // depreciate

        binding.viewModel = viewModel


        binding.apply {
            evBirthday.hint = getString(R.string.birthdayDate)
            btValidate.text = getString(R.string.validate)
        }

        // Code qui remplace la fonction onValidate()
        viewModel.navigateToOtherActivity.observe(viewLifecycleOwner, { user ->
            user?.let {
                val message = viewModel.user.value?.gender + " " + LongConverter.dateToString(
                    viewModel.user.value?.birthdayDate ?: 0
                )
                Toast.makeText(this.context, message, Toast.LENGTH_SHORT).show()

                viewModel.doneValidateNavigating()
            }
        })

        return binding.root
    }

}