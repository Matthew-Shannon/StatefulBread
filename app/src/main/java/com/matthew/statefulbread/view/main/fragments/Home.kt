package com.matthew.statefulbread.view.main.fragments

import com.matthew.statefulbread.core.BaseFragment
import com.matthew.statefulbread.databinding.HomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Home : BaseFragment<HomeBinding>(HomeBinding::inflate)