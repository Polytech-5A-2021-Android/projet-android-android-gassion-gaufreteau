package com.polytech.projet_android_iot.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.polytech.projet_android_iot.R
import com.polytech.projet_android_iot.databinding.FragmentRGBPickerBinding
import com.polytech.projet_android_iot.viewmodel.RGBPickerViewModel
import com.polytech.projet_android_iot.viewmodelfactory.RGBPickerViewModelFactory
import com.azeesoft.lib.colorpicker.ColorPickerDialog
import com.polytech.projet_android_iot.db.DatabaseIotUser


class RGBPickerFragment : Fragment() {

    private lateinit var binding: FragmentRGBPickerBinding
    private lateinit var viewModel: RGBPickerViewModel
    private lateinit var viewModelFactory: RGBPickerViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val application = requireNotNull(this.activity).application
        val dataSource = DatabaseIotUser.getInstance(application).userIOTDao
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_r_g_b_picker, container, false)
        val args = RGBPickerFragmentArgs.fromBundle(requireArguments())
        val uid = args.uid
        val bid = args.bid
        viewModelFactory = RGBPickerViewModelFactory(dataSource,application,uid,bid)
        viewModel = ViewModelProvider(this,viewModelFactory)[RGBPickerViewModel::class.java]

        binding.viewModel = viewModel

        binding.apply {
            tvTitle.text = getString(R.string.rbgpick)
            btValidate.text = getString(R.string.validate)
            btRegister.text = getString(R.string.registerRgb)
            btOpenpicker1.text = getString(R.string.openpicker)
            tvChoosencolor1.text = getString(R.string.choosencolor1)
            btOpenpicker2.text = getString(R.string.openpicker)
            tvChoosencolor2.text = getString(R.string.choosencolor2)
            btOpenpicker3.text = getString(R.string.openpicker)
            tvChoosencolor3.text = getString(R.string.choosencolor3)
        }

        //CP 1
        val colorPickerDialog1 = ColorPickerDialog.createColorPickerDialog(activity)
        colorPickerDialog1.setOnColorPickedListener { color, hex ->
            binding.tvColor1.setBackgroundColor(color)
            viewModel.setColor(1,hex)
        }
        binding.btOpenpicker1.setOnClickListener {
            colorPickerDialog1.show()
        }

        //CP2
        val colorPickerDialog2 = ColorPickerDialog.createColorPickerDialog(activity)
        colorPickerDialog2.setOnColorPickedListener { color, hex ->
            binding.tvColor2.setBackgroundColor(color)
            viewModel.setColor(2,hex)
        }
        binding.btOpenpicker2.setOnClickListener {
            colorPickerDialog2.show()
        }

        //CP 3
        val colorPickerDialog3 = ColorPickerDialog.createColorPickerDialog(activity)
        colorPickerDialog3.setOnColorPickedListener { color, hex ->
            binding.tvColor3.setBackgroundColor(color)
            viewModel.setColor(3,hex)
        }
        binding.btOpenpicker3.setOnClickListener {
            colorPickerDialog3.show()
        }


        viewModel.colors.observe(viewLifecycleOwner, { res ->
            res?.let {
                val message: String
                if(res) {
                    message = "Successfully used colors"
                    this.findNavController().navigate(RGBPickerFragmentDirections.actionRGBPickerFragmentToPersoMenuFragment(uid,bid))
                    viewModel.doneNavigating()
                }else{
                    message = "Problem with selection of the colors"
                }
                Toast.makeText(this.context, message, Toast.LENGTH_SHORT).show()
            }
        })

        binding.btRegister.setOnClickListener {
            this.findNavController().navigate(
                RGBPickerFragmentDirections.actionRGBPickerFragmentToRegisterRgbFragment(uid,bid,viewModel.getColor(1),viewModel.getColor(2),viewModel.getColor(3))
            )
        }

        return binding.root
    }
}