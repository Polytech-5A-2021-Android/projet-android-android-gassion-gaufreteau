package com.polytech.projet_android_iot.fragments.exemples

import com.polytech.projet_android_iot.adapter.MyListAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.polytech.projet_android_iot.R
import com.polytech.projet_android_iot.databinding.FragmentListBinding
import com.polytech.projet_android_iot.db.DBase
import com.polytech.projet_android_iot.model.User
import com.polytech.projet_android_iot.viewmodel.exemples.ListViewModel
import com.polytech.projet_android_iot.viewmodelfactory.exemples.ListViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

class FragmentList :  Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentListBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_list, container, false)

        val application = requireNotNull(this.activity).application

        val dataSource = DBase.getInstance(application).userDao
        val viewModelFactory = ListViewModelFactory(dataSource, application)

        val viewModel = ViewModelProvider(this, viewModelFactory).get(ListViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        val adapter = MyListAdapter()
        binding.list.adapter = adapter

        viewModel.users.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        return binding.root
    }

   companion object {
        @JvmStatic
        @BindingAdapter("userDate")
        fun TextView.setUserDate(item: User) {
            val date = Date(item.birthdayDate)
            val f = SimpleDateFormat("dd/MM/yy")
            val dateText = f.format(date)
            text = dateText
        }


        @BindingAdapter("userImage")
        @JvmStatic
        fun ImageView.setUserImage(item: User) {
            setImageResource(
                when (item.gender) {
                    "Homme" -> R.mipmap.ic_man
                    else -> R.mipmap.ic_woman
                }
            )
        }
    }
}