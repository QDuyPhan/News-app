package com.example.newsapp.ui.news

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp.data.response.NewsResponse
import com.example.newsapp.databinding.ItemNewsBinding
import java.time.format.TextStyle
import java.util.Locale

class NewsAdapter(
    private var news: List<NewsResponse>
) : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemNewsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: NewsResponse) {
            binding.apply {
                val dayOfWeek =
                    item.publishedAt.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
                val day = item.publishedAt.dayOfMonth
                val month = item.publishedAt.month.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
                Glide.with(itemView.context).load(item.image).into(imgNews)
                txtNewsTitle.text = item.title
                txtNewsPublished.text = "$dayOfWeek, $day $month"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return news.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(news[position])
    }

    fun addData(list: List<NewsResponse>) {
        news = list
        notifyDataSetChanged()
    }
}