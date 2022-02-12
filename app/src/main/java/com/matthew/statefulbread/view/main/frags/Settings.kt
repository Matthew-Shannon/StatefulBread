package com.matthew.statefulbread.view.main.frags

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding4.view.clicks
import com.matthew.statefulbread.core.view.BaseFragment
import com.matthew.statefulbread.databinding.CellSettingsBinding
import com.matthew.statefulbread.databinding.SettingsBinding
import com.matthew.statefulbread.repo.*
import com.matthew.statefulbread.repo.model.User
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.kotlin.Singles
import io.reactivex.rxjava3.kotlin.addTo
import javax.inject.Inject

@AndroidEntryPoint
class Settings : BaseFragment<SettingsBinding>(SettingsBinding::inflate) {

    @Inject lateinit var settingsVM: SettingsVM

    private val adapter: SettingsAdapter by lazy { SettingsAdapter(layoutInflater, items) }
    private val items: MutableList<String> by lazy { mutableListOf() }

    override fun onResume() {
        super.onResume()

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)

        binding.logoutButton.clicks()
            .flatMapCompletable { settingsVM.onLogout() }
            .subscribe().addTo(disposable)

        settingsVM.getUser()
            .doOnSuccess(::setupRecyclerView)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe().addTo(disposable)

        settingsVM.getDayNightMode()
            .doOnSuccess(::setupDarkmodeCheckbox)
            .subscribe().addTo(disposable)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setupRecyclerView(items: List<String>) {
        this.items.clear()
        this.items.addAll(items)
        adapter.notifyDataSetChanged()
    }

    private fun setupDarkmodeCheckbox(mode: Boolean) {
        binding.nightModeCheckbox.isChecked = mode
        binding.nightModeCheckbox.clicks()
            .flatMapCompletable { settingsVM.toggleDayNightMode() }
            .subscribe().addTo(disposable)
    }

}

class SettingsAdapter(private val inflater: LayoutInflater, private val items: List<String>) : RecyclerView.Adapter<SettingsAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: CellSettingsBinding) : RecyclerView.ViewHolder(binding.root) { fun bind(value: String) { binding.cellTextView.text = value } }
    override fun onCreateViewHolder(parent: ViewGroup, type: Int): ViewHolder = ViewHolder(CellSettingsBinding.inflate(inflater, parent, false))
    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])
    override fun getItemCount(): Int = items.size
}

class SettingsVM @Inject constructor(private val prefs: IPrefs, private val storage: IStorage, private val theme: ITheme, @MainNav private val nav: INav) {

    fun getUser(): Single<List<String>> = prefs.getOwnerID()
        .flatMapMaybe { id -> storage.userRepo().flatMapMaybe { it.findById(id) } }
        .map(User::toList).defaultIfEmpty(emptyList())

    fun onLogout(): Completable = Completable.mergeArray(
        Completable.defer(storage::clear),
        Completable.defer(prefs::clear),
        Completable.defer(nav::toSplash)
    )

    fun getDayNightMode(): Single<Boolean> = theme.getDarkMode()

    fun toggleDayNightMode(): Completable = theme.toggleDarkMode()

}
