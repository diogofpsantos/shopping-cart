package com.hostel.shoppingcart.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.hostel.shoppingcart.R
import com.hostel.shoppingcart.databinding.FragmentCheckoutBinding
import kotlinx.android.synthetic.main.fragment_checkout.*
import kotlinx.android.synthetic.main.fragment_home.*

class CheckoutFragment : Fragment() {

    private var binding: FragmentCheckoutBinding? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MainActivity).mainComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_checkout, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.let {
            back_btn.setOnClickListener { findNavController().navigate(CheckoutFragmentDirections.navigateCheckoutToHome()) }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}