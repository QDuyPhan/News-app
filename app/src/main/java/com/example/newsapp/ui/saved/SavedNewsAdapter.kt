package com.example.newsapp.ui.saved

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp.data.local.entity.NewsEntity
import com.example.newsapp.databinding.ItemNewsBinding
import com.example.newsapp.utils.Logger
import com.example.newsapp.utils.setOnSingClickListener
import java.time.format.TextStyle
import java.util.Locale

class SavedNewsAdapter(
) : RecyclerView.Adapter<SavedNewsAdapter.ViewHolder>() {
    private val savedNews = mutableListOf<NewsEntity>()

    inner class ViewHolder(private val binding: ItemNewsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: NewsEntity) {
            binding.apply {
                val dayOfWeek =
                    item.publishedAt.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
                val day = item.publishedAt.dayOfMonth
                val month = item.publishedAt.month.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
                Glide.with(itemView.context).load(item.image).into(imgNews)
                txtNewsTitle.text = item.title
                txtNewsPublished.text = "$dayOfWeek, $day $month"

                root.setOnSingClickListener {
                    onItemClickListener?.let {
                        it(item)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return savedNews.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(savedNews[position])
    }

    private var onItemClickListener: ((NewsEntity) -> Unit)? = null
    fun setOnItemClickListener(listener: (NewsEntity) -> Unit) {
        onItemClickListener = listener
    }

    fun addData(list: List<NewsEntity>) {
        savedNews.clear()
        savedNews.addAll(list)
        Logger.logI("List news: $savedNews")
        notifyDataSetChanged()
    }
}