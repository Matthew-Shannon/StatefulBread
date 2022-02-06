package com.matthew.statefulbread.view.main.fragments

import com.matthew.statefulbread.core.BaseFragment
import com.matthew.statefulbread.databinding.FavoritesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Favorites : BaseFragment<FavoritesBinding>(FavoritesBinding::inflate)