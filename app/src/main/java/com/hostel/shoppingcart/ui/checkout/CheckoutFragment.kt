package com.hostel.shoppingcart.ui.checkout

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.hostel.shoppingcart.R
import com.hostel.shoppingcart.data.model.CartItem
import com.hostel.shoppingcart.databinding.FragmentCheckoutBinding
import com.hostel.shoppingcart.ui.MainActivity
import com.hostel.shoppingcart.ui.MainViewModel
import com.hostel.shoppingcart.utils.extensions.showInfoDialog
import java.text.NumberFormat
import java.util.*
import javax.inject.Inject

class CheckoutFragment : Fragment() {

    private var binding: FragmentCheckoutBinding? = null

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private val mainViewModel: MainViewModel by activityViewModels { factory }

    private var cartItemList: List<CartItem>? = null

    private val cartItemsAdapter = CheckoutItemsAdapter()

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
        binding?.let { binding ->
            binding.payBtn.setOnClickListener {
                findNavController().navigate(
                    CheckoutFragmentDirections.navigateCheckoutToHome()
                )
            }
            mainViewModel.getCurrencies()
            mainViewModel.loading.observe(viewLifecycleOwner) {
                binding.price = mainViewModel.totalPrice.value
                binding.beds = if (mainViewModel.totalBeds.value!! > 9)
                    (mainViewModel.totalBeds.value.toString()) else String.format(
                    "0%d",
                    mainViewModel.totalBeds.value
                )
                val currencyFormat = NumberFormat.getCurrencyInstance()
                currencyFormat.currency = Currency.getInstance(mainViewModel.getDefaultCurrency())
                binding.currencyFormat = currencyFormat
                if (!it) {
                    binding.group1.visibility = View.VISIBLE
                    binding.loadingLayout.visibility = View.GONE
                    cartItemList = mainViewModel.cartItems.value
                    cartItemList?.let { it1 -> cartItemsAdapter.submitList(it1) }
                    binding.itemsRv.adapter = cartItemsAdapter
                } else {
                    binding.group1.visibility = View.GONE
                    binding.loadingLayout.visibility = View.VISIBLE
                }
            }
            mainViewModel.errorEvent.observe(viewLifecycleOwner){
                binding.showInfoDialog(it.message, clickListener = {

                } )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}