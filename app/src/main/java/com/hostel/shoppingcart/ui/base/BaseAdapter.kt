package com.hostel.shoppingcart.ui.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseAdapter<T : Any, VB : ViewDataBinding>(@LayoutRes private val resId: Int) :
    RecyclerView.Adapter<ViewHolder<VB>>() {

    private lateinit var inflater: LayoutInflater
    val currentList = mutableListOf<T>()

    abstract fun bind(binding: VB, item: T, position:Int)

    open fun submitList(list: List<T>) {
        currentList.clear()
        currentList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<VB> {
        if (!::inflater.isInitialized) {
            inflater = LayoutInflater.from(parent.context)
        }
        return ViewHolder(DataBindingUtil.inflate(inflater, resId, parent, false))
    }

    override fun getItemCount() = currentList.size

    override fun onBindViewHolder(holder: ViewHolder<VB>, position: Int) {
        bind(holder.binding, currentList[position], position)
    }
}

class ViewHolder<VB : ViewBinding>(val binding: VB) :
    RecyclerView.ViewHolder(binding.root)
