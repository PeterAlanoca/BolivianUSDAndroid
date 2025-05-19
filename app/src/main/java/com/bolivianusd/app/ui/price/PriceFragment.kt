package com.bolivianusd.app.ui.price

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bolivianusd.app.core.base.BaseFragment
import com.bolivianusd.app.databinding.FragmentPriceBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.bolivianusd.app.R
import com.bolivianusd.app.data.repository.entity.enum.OperationType
import com.bolivianusd.app.ui.price.adapter.PricePagerAdapter
import kotlin.getValue

class PriceFragment : BaseFragment<FragmentPriceBinding>() {

    private val fragments by lazy {
        listOf(
            PriceItemPagerFragment.newInstance(OperationType.BUY),
            PriceItemPagerFragment.newInstance(OperationType.SELL)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isNotRecreate = false
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentPriceBinding.inflate(inflater, container, false)

    override fun initViews() {
        setupViewPager()
    }

    private fun setupViewPager() = with(binding) {
        val titles = listOf(
            getString(R.string.price_view_pager_item_buy),
            getString(R.string.price_view_pager_item_sell)
        )
        viewPager.adapter = PricePagerAdapter(this@PriceFragment, fragments)
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = titles[position]
        }.attach()
    }

    companion object {
        const val TAG = "PriceFragment"
        fun newInstance() = PriceFragment()
    }
}