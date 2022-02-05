package com.matthew.statefulbread.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.matthew.statefulbread.core.BaseActivity
import com.matthew.statefulbread.databinding.ActivitySettingsBinding
import com.matthew.statefulbread.databinding.CellSettingsBinding
import com.matthew.statefulbread.service.IPrefs

class Settings : BaseActivity() {

    private lateinit var binding: ActivitySettingsBinding

    private val items: List<String> by lazy { getData(prefs) }

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Settings"
    }

    override fun onStart() {
        super.onStart()
        binding.recyclerView.adapter = SettingsAdapter(layoutInflater, items)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.logoutButton.setOnClickListener { nav.toLogout(this) }
    }

    companion object {
        fun getData(prefs: IPrefs) : List<String> = listOf(
            "Name: "+prefs.getString("name"),
            "Email: "+prefs.getString("email"),
            "Zip Code: "+prefs.getString("zipCode"),
            "Password: "+prefs.getString("password"),
        )
    }

}

class SettingsAdapter(private val inflater: LayoutInflater, private val items: List<String>) : RecyclerView.Adapter<SettingsAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: CellSettingsBinding) : RecyclerView.ViewHolder(binding.root) { fun bind(value: String) { binding.cellTextView.text = value } }
    override fun onCreateViewHolder(parent: ViewGroup, type: Int): ViewHolder = ViewHolder(CellSettingsBinding.inflate(inflater, parent, false))
    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])
    override fun getItemCount(): Int = items.size

}
