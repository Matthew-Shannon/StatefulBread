package com.matthew.statefulbread.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.matthew.statefulbread.App
import com.matthew.statefulbread.TAG
import com.matthew.statefulbread.databinding.ActivitySettingsBinding
import com.matthew.statefulbread.databinding.CellSettingsBinding
import com.matthew.statefulbread.service.IPrefs

class Settings : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private val prefs: IPrefs by lazy { App.castToApp(this).prefs }
    private val items: List<String> by lazy {
        listOf(
            "Name: "+prefs.getString("name"),
            "Email: "+prefs.getString("email"),
            "Zip Code: "+prefs.getString("zipCode"),
            "Password: "+prefs.getString("password"),
        )
    }

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        Log.d(TAG, "onCreate")
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Settings"

    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")
        binding.recyclerView.adapter = SettingsAdapter(layoutInflater, items)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.logoutButton.setOnClickListener { onLogout() }
    }

    private fun onLogout() {
        prefs.clear()
        startActivity(Intent(this, Login::class.java))
    }

}

class SettingsAdapter(private val inflater: LayoutInflater, private val items: List<String>) : RecyclerView.Adapter<SettingsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, type: Int): ViewHolder = ViewHolder(CellSettingsBinding.inflate(inflater, parent, false))
    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])
    override fun getItemCount(): Int = items.size

    inner class ViewHolder(private val binding: CellSettingsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(value: String) {
            binding.cellTextView.text = value
        }
    }

}
