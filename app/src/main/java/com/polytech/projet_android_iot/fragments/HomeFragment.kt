package com.polytech.projet_android_iot.fragments

import com.polytech.projet_android_iot.adapter.MyListAdapterBoard
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.polytech.projet_android_iot.R
import com.polytech.projet_android_iot.adapter.BoardListener
import com.polytech.projet_android_iot.databinding.FragmentHomeBinding
import com.polytech.projet_android_iot.db.DatabaseIotBoard
import com.polytech.projet_android_iot.db.DatabaseIotUser
import com.polytech.projet_android_iot.viewmodel.HomeViewModel
import com.polytech.projet_android_iot.viewmodelfactory.HomeViewModelFactory

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var viewModelFactory: HomeViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val application = requireNotNull(this.activity).application
        val dataSource = DatabaseIotUser.getInstance(application).userIOTDao
        val dataSourceBoard = DatabaseIotBoard.getInstance(application).boardIOTDao
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        val args = HomeFragmentArgs.fromBundle(requireArguments())
        val uid = args.uid
        binding.lifecycleOwner = this

        viewModelFactory = HomeViewModelFactory(dataSource,dataSourceBoard,application,uid)
        viewModel = ViewModelProvider(this,viewModelFactory)[HomeViewModel::class.java]
        binding.viewModel = viewModel

        val adapter = MyListAdapterBoard(BoardListener { boardid ->
            //Toast.makeText(this.context,"$boardid clicked", Toast.LENGTH_SHORT).show()
            this.findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToPersoMenuFragment(uid,boardid)
            )
        })
        binding.liBoardlist.adapter = adapter

        viewModel.boards.observe(viewLifecycleOwner, {
            it?.let {
                adapter.submitList(it)
            }
        })

        binding.apply {
            tvTitle.text = getString(R.string.boardList)
            btAddBoard.text = getString(R.string.connectBoard)
            btParams.text = getString(R.string.params)
        }

        binding.btAddBoard.setOnClickListener {
            this.findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToBoardConnectFragment(uid)
            )
        }

        binding.btParams.setOnClickListener {
            this.findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToParamsFragment(uid)
            )
        }

        return binding.root
    }


}