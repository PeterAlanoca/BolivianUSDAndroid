package com.bolivianusd.app.ui.price

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bolivianusd.app.databinding.FragmentPriceBinding
import com.google.android.material.tabs.TabLayoutMediator

class PriceFragment : Fragment() {

    private lateinit var binding: FragmentPriceBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPriceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val titles = listOf("DOLAR PARALELO", "DOLAR OFICIAL")
        val fragments = listOf(PriceParallelFragment.newInstance(), PriceOfficialFragment.newInstance())

        binding.viewPager.adapter = PagerAdapter(this, fragments)

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = titles[position]
        }.attach()

    }


    companion object {
        const val TAG = "PriceFragment"
        fun newInstance() = PriceFragment()
    }
}