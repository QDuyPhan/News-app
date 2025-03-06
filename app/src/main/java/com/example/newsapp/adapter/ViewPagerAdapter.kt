package com.example.newsapp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.newsapp.data.response.CategoryResponse
import com.example.newsapp.ui.news.NewsFragment
import com.example.newsapp.utils.Constants.BAT_DONG_SAN
import com.example.newsapp.utils.Constants.CONG_NGHE
import com.example.newsapp.utils.Constants.DOI_SONG
import com.example.newsapp.utils.Constants.DU_LICH
import com.example.newsapp.utils.Constants.GIAI_TRI
import com.example.newsapp.utils.Constants.GIAO_DUC
import com.example.newsapp.utils.Constants.GOC_NHIN
import com.example.newsapp.utils.Constants.KHOA_HOC
import com.example.newsapp.utils.Constants.KINH_DOANH
import com.example.newsapp.utils.Constants.PHAP_LUAT
import com.example.newsapp.utils.Constants.PODCASTS
import com.example.newsapp.utils.Constants.SUC_KHOE
import com.example.newsapp.utils.Constants.TAM_SU
import com.example.newsapp.utils.Constants.THE_GIOI
import com.example.newsapp.utils.Constants.THE_THAO
import com.example.newsapp.utils.Constants.THOI_SU
import com.example.newsapp.utils.Constants.THU_GIAN
import com.example.newsapp.utils.Constants.VIDEO
import com.example.newsapp.utils.Constants.XE
import com.example.newsapp.utils.Constants.Y_KIEN
import com.example.newsapp.utils.Logger

class ViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val listCategory: List<CategoryResponse>? = null
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun createFragment(position: Int): Fragment {
        return NewsFragment(listCategory?.get(position)?.name)
    }

    override fun getItemCount(): Int {
        return listCategory!!.size
    }
}