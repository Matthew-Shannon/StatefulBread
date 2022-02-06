package com.matthew.statefulbread.view.main.frags

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.matthew.statefulbread.core.view.BaseFragment
import com.matthew.statefulbread.core.toggleNightMode
import com.matthew.statefulbread.databinding.CellSettingsBinding
import com.matthew.statefulbread.databinding.SettingsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Settings : BaseFragment<SettingsBinding>(SettingsBinding::inflate) {

    private val items: List<String> by lazy { dataService.getUser() }

    override fun onResume() {
        super.onResume()
        binding.recyclerView.adapter = SettingsAdapter(layoutInflater, items)
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        binding.logoutButton.setOnClickListener { prefsService.clear(); navService.toSplash() }
        binding.dayNightButton.setOnClickListener { activity?.toggleNightMode() }
    }

}

class SettingsAdapter(private val inflater: LayoutInflater, private val items: List<String>) : RecyclerView.Adapter<SettingsAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: CellSettingsBinding) : RecyclerView.ViewHolder(binding.root) { fun bind(value: String) { binding.cellTextView.text = value } }
    override fun onCreateViewHolder(parent: ViewGroup, type: Int): ViewHolder = ViewHolder(CellSettingsBinding.inflate(inflater, parent, false))
    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])
    override fun getItemCount(): Int = items.size
}


