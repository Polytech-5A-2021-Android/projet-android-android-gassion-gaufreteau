package com.polytech.projet_android_iot.fragments.exemples

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.polytech.projet_android_iot.R
import com.polytech.projet_android_iot.adapter.MyApiListAdapter
import com.polytech.projet_android_iot.databinding.FragmentApiListBinding
import com.polytech.projet_android_iot.viewmodel.exemples.ApiListViewModel
import com.polytech.projet_android_iot.viewmodelfactory.exemples.ApiListViewModelFactory

class ApiListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentApiListBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_api_list, container, false
        )

        val application = requireNotNull(this.activity).application

        val viewModelFactory = ApiListViewModelFactory(application)

        var viewModel = ViewModelProvider(this,viewModelFactory).get(ApiListViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        val adapter = MyApiListAdapter()
        binding.list.adapter = adapter

        viewModel.response.observe(viewLifecycleOwner, Observer {
            it?.let {
                //adapter.data = it
                adapter.submitList(it)
            }
        })
        return binding.root
    }
}