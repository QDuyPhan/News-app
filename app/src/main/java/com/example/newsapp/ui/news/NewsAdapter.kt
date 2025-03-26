package com.example.newsapp.ui.news

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp.data.remote.response.NewsResponse
import com.example.newsapp.databinding.ItemNewsBinding
import com.example.newsapp.utils.setOnSingClickListener
import java.time.format.TextStyle
import java.util.Locale

class NewsAdapter(
    private var news: List<NewsResponse>
) : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    var isSelectMode = false
    private val selectedItems = mutableSetOf<NewsResponse>()
    private var onItemClickListener: ((NewsResponse) -> Unit)? = null

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

                checkboxSelect.visibility = if (isSelectMode) View.VISIBLE else View.GONE
                checkboxSelect.isChecked = selectedItems.contains(item)

                checkboxSelect.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) selectedItems.add(item) else selectedItems.remove(item)
                }

                root.setOnSingClickListener {
                    if (isSelectMode) {
                        checkboxSelect.isChecked = !checkboxSelect.isChecked
                    } else {
                        onItemClickListener?.invoke(item)
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
        return news.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(news[position])
    }

    fun setOnItemClickListener(listener: (NewsResponse) -> Unit) {
        onItemClickListener = listener
    }

    fun addData(list: List<NewsResponse>) {
        news = list
        notifyDataSetChanged()
    }

    fun enableSelectionMode(enable: Boolean) {
        isSelectMode = enable
        if (!enable) selectedItems.clear()
        notifyDataSetChanged()
    }

    fun getCurrentList(): List<NewsResponse> {
        return news
    }

    fun getSelectedItems(): List<NewsResponse> = selectedItems.toList()
}