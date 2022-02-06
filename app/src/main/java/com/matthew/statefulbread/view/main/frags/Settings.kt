package com.matthew.statefulbread.view.main.frags

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.matthew.statefulbread.core.setNightMode
import com.matthew.statefulbread.core.view.BaseFragment
import com.matthew.statefulbread.databinding.CellSettingsBinding
import com.matthew.statefulbread.databinding.SettingsBinding
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.kotlin.addTo

@AndroidEntryPoint
class Settings : BaseFragment<SettingsBinding>(SettingsBinding::inflate) {

    private val adapter: SettingsAdapter by lazy { SettingsAdapter(layoutInflater, items) }
    private val items: MutableList<String> by lazy { mutableListOf() }

    override fun onResume() {
        super.onResume()

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)

        dataService.getUser()
            .subscribe(::setupRecyclerView)
            .addTo(disposable)

        prefsService.getDarkModeSingle()
            .subscribe(::setupDarkmodeCheckbox)
            .addTo(disposable)

        binding.logoutButton.setOnClickListener { onLogout() }

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setupRecyclerView(items: List<String>) {
        this.items.clear()
        this.items.addAll(items)
        adapter.notifyDataSetChanged()
    }

    private fun setupDarkmodeCheckbox(mode: Boolean) {
        binding.nightModeCheckbox.isChecked = mode
        binding.nightModeCheckbox.setOnClickListener { onNightModeToggle() }
    }

    private fun onNightModeToggle() {
        val mode = !prefsService.getDarkMode()
        prefsService.setDarkMode(mode)
        activity?.setNightMode(mode)
    }

    private fun onLogout() {
        prefsService.clear()
        navService.toSplash()
    }

}

class SettingsAdapter(private val inflater: LayoutInflater, private val items: List<String>) : RecyclerView.Adapter<SettingsAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: CellSettingsBinding) : RecyclerView.ViewHolder(binding.root) { fun bind(value: String) { binding.cellTextView.text = value } }
    override fun onCreateViewHolder(parent: ViewGroup, type: Int): ViewHolder = ViewHolder(CellSettingsBinding.inflate(inflater, parent, false))
    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])
    override fun getItemCount(): Int = items.size
}


