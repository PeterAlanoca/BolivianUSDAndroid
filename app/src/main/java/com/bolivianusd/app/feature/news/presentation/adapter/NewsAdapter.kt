package com.bolivianusd.app.feature.news.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bolivianusd.app.databinding.ItemNewsBinding
import com.bolivianusd.app.feature.news.presentation.model.News

class NewsAdapter(private val newsItems: List<News> = emptyList()) :
    RecyclerView.Adapter<NewsAdapter.NewsHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsHolder {
        context = parent.context
        val binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsHolder(binding)
    }

    override fun getItemCount(): Int {
        return newsItems.size
    }

    override fun onBindViewHolder(holder: NewsHolder, position: Int) {
        holder.bind(newsItems[position])
    }

    inner class NewsHolder(private val binding: ItemNewsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(news: News) = with(binding) {

        }

    }


}