package com.bolivianusd.app.feature.news.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bolivianusd.app.databinding.FragmentNewsBinding
import com.bolivianusd.app.feature.news.presentation.adapter.NewsAdapter
import com.bolivianusd.app.feature.news.presentation.model.News

class NewsFragment : Fragment() {

    private lateinit var binding: FragmentNewsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val newsItems = listOf(
            News(),
            News(),
            News(),
            News(),
            News(),
            News(),
            News(),
            News(),
            News(),
            News(),
            News(),
            News(),
            News()
        )

        binding.newsRecyclerView.adapter = NewsAdapter(newsItems)
    }

    companion object {
        const val TAG = "NewsFragment"
        fun newInstance() = NewsFragment()
    }
}