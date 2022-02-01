package com.hostel.shoppingcart.ui.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.hostel.shoppingcart.R
import com.hostel.shoppingcart.data.model.Dorm
import com.hostel.shoppingcart.databinding.FragmentHomeBinding
import com.hostel.shoppingcart.ui.MainActivity
import com.hostel.shoppingcart.ui.MainViewModel
import javax.inject.Inject

class HomeFragment : Fragment() {

    private var binding: FragmentHomeBinding? = null

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private val mainViewModel: MainViewModel by activityViewModels { factory }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MainActivity).mainComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.let { binding ->
            binding.itemsRv.layoutManager = LinearLayoutManager(requireContext())
            mainViewModel.loading.observe(viewLifecycleOwner) {
                if (!it) {
                    binding.group1.visibility = View.VISIBLE
                    binding.loadingLayout.visibility = View.GONE
                    val cartItemsAdapter = CartItemsAdapter(
                        object : CartItemsAdapter.OptionsListener {
                            override fun addToCart(item: Dorm) {
                                mainViewModel.addBedToCart(item)
                            }

                            override fun removeFromCart(item: Dorm) {
                                item.id?.let { it1 -> mainViewModel.removeBedFromCart(it1) }
                            }
                        }
                    )
                    mainViewModel.dorms.let { it1 -> cartItemsAdapter.submitList(it1) }
                    binding.itemsRv.adapter = cartItemsAdapter
                } else {
                    binding.group1.visibility = View.GONE
                    binding.loadingLayout.visibility = View.VISIBLE
                }
            }
            mainViewModel.totalPrice.observe(viewLifecycleOwner) {
                Log.d("TotalPrice", "Price: $it")
                binding.checkoutBtn.isEnabled = it > 0
            }
            binding.checkoutBtn.setOnClickListener {
                findNavController().navigate(
                    HomeFragmentDirections.actionCheckout()
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}