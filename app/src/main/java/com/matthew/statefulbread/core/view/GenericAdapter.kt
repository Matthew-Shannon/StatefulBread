package com.matthew.statefulbread.core.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.matthew.statefulbread.core.Binder

open class GenericAdapter<T, B: ViewBinding>(
    private val items: List<T>,
    private val inflater: LayoutInflater,
    private val binder: Binder<B>,
    private val painter: (T, B) -> Unit
): RecyclerView.Adapter<GenericAdapter<T, B>.ViewHolder>() {
    inner class ViewHolder(private val binding: B) : RecyclerView.ViewHolder(binding.root) { fun bind(req: T) { painter(req, binding) } }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(binder(inflater))
    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])
    override fun getItemCount(): Int = items.size
}