package com.bolivianusd.app.core.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.bolivianusd.app.R

abstract class BaseFragment<T : ViewBinding> : Fragment() {

    private var _binding: T? = null
    var isNotRecreate = false
    val binding
        get() = _binding
            ?: throw IllegalStateException(getString(R.string.app_fragment_not_attached_error))

    protected abstract fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): T

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (isNotRecreate) {
            if (_binding == null) {
                _binding = getViewBinding(inflater, container)
                initCreateView()
            }
        } else {
            _binding = getViewBinding(inflater, container)
        }
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configTitle()
        initViews()
        initData()
        setListeners()
    }

    override fun onStart() {
        super.onStart()
        initDataStart()
    }

    override fun onResume() {
        super.onResume()
        initViewsResume()
        initDataResume()
        setListenersResume()
        configBottomBar()
    }

    open fun initCreateView() {}

    open fun configTitle() {}

    open fun initViewsResume() {}

    open fun setListenersResume() {}

    open fun initViews() {}

    open fun setListeners() {}

    open fun initData() {}

    open fun initDataStart() {}

    open fun initDataResume() {}

    open fun configBottomBar() {}

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}