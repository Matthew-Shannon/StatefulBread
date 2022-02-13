package com.matthew.statefulbread.view.main.frags

import android.annotation.SuppressLint
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding4.view.clicks
import com.matthew.statefulbread.core.view.GenericAdapter
import com.matthew.statefulbread.core.view.BaseFragment
import com.matthew.statefulbread.core.view.INav
import com.matthew.statefulbread.core.view.MainNav
import com.matthew.statefulbread.databinding.CellSettingsBinding
import com.matthew.statefulbread.databinding.SettingsBinding
import com.matthew.statefulbread.repo.IPrefs
import com.matthew.statefulbread.repo.IStorage
import com.matthew.statefulbread.repo.ITheme
import com.matthew.statefulbread.repo.model.User
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.kotlin.addTo
import javax.inject.Inject

typealias Field = Map.Entry<String,String>

@AndroidEntryPoint
@SuppressLint("SetTextI18n", "NotifyDataSetChanged")
class Settings : BaseFragment<SettingsBinding>(SettingsBinding::inflate) {

    @Inject lateinit var settingsVM: SettingsVM

    private val adapter: GenericAdapter<Field,CellSettingsBinding> by lazy { GenericAdapter(items, layoutInflater, CellSettingsBinding::inflate, ::onDrawCell)  }
    private val items: MutableList<Field> by lazy { mutableListOf() }

    override fun onResume() {
        super.onResume()

        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        binding.recyclerView.adapter = adapter

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

    private fun onDrawCell(field: Field, b: CellSettingsBinding) {
        b.cellTextView.text = "${field.key}: ${field.value}"
    }

    private fun setupRecyclerView(items: Map<String,String>) {
        this.items.clear()
        this.items.addAll(items.entries)
        adapter.notifyDataSetChanged()
    }

    private fun setupDarkmodeCheckbox(mode: Boolean) {
        binding.nightModeCheckbox.isChecked = mode
        binding.nightModeCheckbox.clicks()
            .flatMapCompletable { settingsVM.toggleDayNightMode() }
            .subscribe().addTo(disposable)
    }

}

class SettingsVM @Inject constructor(private val prefs: IPrefs, private val storage: IStorage, private val theme: ITheme, @MainNav private val nav: INav) {

    fun getUser(): Single<Map<String,String>> = prefs.getOwnerEmail()
        .flatMapMaybe { email -> storage.userRepo().flatMapMaybe { it.findByEmail(email) } }
        .map(User::explode).defaultIfEmpty(mapOf())

    fun onLogout(): Completable = Completable.mergeArray(
        Completable.defer(storage::clear),
        Completable.defer(prefs::clear),
        Completable.defer(nav::toSplash)
    )

    fun getDayNightMode(): Single<Boolean> = prefs.getDayNightMode()

    fun toggleDayNightMode(): Completable = theme.toggleDayNightMode()

}
