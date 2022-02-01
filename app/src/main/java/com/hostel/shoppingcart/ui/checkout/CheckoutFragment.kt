package com.hostel.shoppingcart.ui.checkout

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.hostel.shoppingcart.R
import com.hostel.shoppingcart.data.model.CartItem
import com.hostel.shoppingcart.data.model.NetworkStatsResponse
import com.hostel.shoppingcart.databinding.FragmentCheckoutBinding
import com.hostel.shoppingcart.ui.MainActivity
import com.hostel.shoppingcart.ui.MainViewModel
import com.hostel.shoppingcart.utils.extensions.showInfoDialog
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
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
                mainViewModel.pay()
                findNavController().navigate(
                    CheckoutFragmentDirections.navigateCheckoutToHome()
                )
            }
            mainViewModel.getCurrencies()
            mainViewModel.loading.observe(viewLifecycleOwner) {
                if (!it) {
                    binding.group1.visibility = View.VISIBLE
                    binding.loadingLayout.visibility = View.GONE
                    binding.itemsRv.adapter = cartItemsAdapter
                } else {
                    binding.group1.visibility = View.GONE
                    binding.loadingLayout.visibility = View.VISIBLE
                }

                binding.currencySpinner.adapter = ArrayAdapter(
                    requireContext(),
                    R.layout.item_spinner,
                    mainViewModel.getCurrenciesForSpinner()
                )
                binding.currencySpinner.setSelection(mainViewModel.getActualCurrencyIndex())
                binding.currencySpinner.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            if (mainViewModel.currencySelected.value!!.currency != mainViewModel.currencies[position].currency)
                                mainViewModel.currencySelected.postValue(mainViewModel.currencies[position])
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {
                        }

                    }
                updateLayout()
            }
            mainViewModel.currencySelected.observe(viewLifecycleOwner) {
                mainViewModel.updateCartCurrencies()
            }
            mainViewModel.totalPrice.observe(viewLifecycleOwner) {
                updateLayout()
            }
            mainViewModel.errorEvent.observe(viewLifecycleOwner) {
                mainViewModel.updateStats(it.stats)
                binding.showInfoDialog(it.message, clickListener = {
                })
            }
        }
    }

    private fun updateLayout() {
        binding?.let { binding ->
            binding.price = mainViewModel.totalPrice.value
            binding.beds = if (mainViewModel.totalBeds.value!! > 9)
                (mainViewModel.totalBeds.value.toString()) else String.format(
                "0%d",
                mainViewModel.totalBeds.value
            )
            val currencyFormat = NumberFormat.getCurrencyInstance()
            currencyFormat.currency =
                Currency.getInstance(mainViewModel.currencySelected.value!!.currency)
            binding.currencyFormat = currencyFormat
            cartItemList = mainViewModel.cartItems.value
            cartItemList?.let { it1 -> cartItemsAdapter.submitList(it1) }
        }
    }

    override fun onResume() {
        super.onResume()
        EventBus.getDefault().register(this)
    }

    override fun onPause() {
        super.onPause()
        EventBus.getDefault().unregister(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onNewIntent(intent: Intent?) {
        if (intent != null) {
            val networkStatsResponse = NetworkStatsResponse.parseIntent(intent)
            Log.d("TAG", "onNewIntent: " + networkStatsResponse.action)
            mainViewModel.updateStats(networkStatsResponse)
        }
        EventBus.getDefault().removeStickyEvent(intent)
    }

}