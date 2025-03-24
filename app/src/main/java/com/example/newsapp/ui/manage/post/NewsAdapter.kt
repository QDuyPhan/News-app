package com.example.newsapp.ui.manage.post

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp.data.local.entity.NewsItem
import com.example.newsapp.data.remote.response.NewsResponse
import com.example.newsapp.databinding.ItemNewsManageBinding
import com.example.newsapp.utils.setOnSingClickListener
import java.time.format.TextStyle
import java.util.Locale

class NewsAdapter(
    private var news: List<NewsItem>
) : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemNewsManageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: NewsItem) {
            binding.apply {
                val dayOfWeek =
                    item.news.publishedAt.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
                val day = item.news.publishedAt.dayOfMonth
                val month =
                    item.news.publishedAt.month.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
                Glide.with(itemView.context).load(item.news.image).into(imgNews)
                txtNewsTitle.text = item.news.title
                txtNewsPublished.text = "$dayOfWeek, $day $month"

                checkbox.isChecked = item.isSelected
                checkbox.setOnCheckedChangeListener { _, isChecked ->
                    item.isSelected = isChecked
                }

                root.setOnSingClickListener {
                    onItemClickListener?.let {
                        it(item.news)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemNewsManageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return news.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(news[position])
    }

    private var onItemClickListener: ((NewsResponse) -> Unit)? = null
    fun setOnItemClickListener(listener: (NewsResponse) -> Unit) {
        onItemClickListener = listener
    }

    fun setData(list: List<NewsItem>) {
        news = list.toMutableList()
        notifyDataSetChanged()
    }

    fun addData(list: List<NewsItem>) {
        news = list
        notifyDataSetChanged()
    }
}