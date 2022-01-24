package com.polytech.projet_android_iot.fragments.exemples

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.polytech.projet_android_iot.R
import com.polytech.projet_android_iot.databinding.FragmentIdentityBinding
import com.polytech.projet_android_iot.db.DBase
import com.polytech.projet_android_iot.viewmodel.exemples.IdentityViewModel
import com.polytech.projet_android_iot.viewmodelfactory.exemples.IdentityViewModelFactory

class IdentityFragment : Fragment() {

    private lateinit var binding: FragmentIdentityBinding
    private lateinit var viewModel: IdentityViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val application = requireNotNull(this.activity).application
        val dataSource = DBase.getInstance(application).userDao
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_identity, container, false)
        binding.lifecycleOwner = this
        val viewModelFactory = IdentityViewModelFactory(dataSource, application)

        viewModel = ViewModelProvider(this,viewModelFactory).get(IdentityViewModel::class.java)
        binding.viewModel = viewModel


        binding.apply {
            tvTitle.text = getString(R.string.title)
            tiFirstname.hint = getString(R.string.firstname)
            tiLastname.hint = getString(R.string.lastname)
            btValidate.text = getString(R.string.validate)
        }


        viewModel.navigateToPersonalDataFragment.observe(viewLifecycleOwner, { user ->
            user?.let {
                this.findNavController().navigate(
                    IdentityFragmentDirections
                        .actionIdentityFragmentToPersonalDataFragment(user))

                viewModel.doneNavigating()
            }
        })

        binding.btList.setOnClickListener {
            this.findNavController().navigate(
                IdentityFragmentDirections
                    .actionIdentityFragmentToFragmentList()
            )
        }

        binding.btApiList.setOnClickListener {
            this.findNavController().navigate(
                IdentityFragmentDirections
                    .actionIdentityFragmentToApiListFragment()
            )
        }

        return binding.root
    }


}
