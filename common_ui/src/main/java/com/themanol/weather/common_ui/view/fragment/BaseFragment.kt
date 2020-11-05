package com.themanol.weather.common_ui.view.fragment

import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

open class BaseFragment<T> : Fragment()
        where T : ViewBinding {

    private var _binding: T? = null

    protected var binding: T
        get() = requireBinding()
        set(value) {
            _binding = value
        }

    private fun requireBinding(): T {
        _binding?.let {
            return it
        }
            ?: throw IllegalStateException("Binding is null, check the status of your fragment ${this::class}")
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
