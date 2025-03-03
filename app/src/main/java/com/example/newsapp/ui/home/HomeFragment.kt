package com.example.newsapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentHomeBinding
import com.example.newsapp.utils.Logger
import com.example.newsapp.utils.Status
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager2: ViewPager2
    private lateinit var adapter: ViewPagerAdapter
    private var listCategories: List<String> = emptyList()
    private val viewModel by viewModels<HomeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        try {
            binding = FragmentHomeBinding.inflate(inflater, container, false)
            drawerLayout = binding.drawerLayout
            navView = binding.navView
            val toolbar = binding.toolbar
            val toggle = ActionBarDrawerToggle(
                requireActivity(),
                drawerLayout,
                toolbar,
                R.string.open_nav,
                R.string.close_nav
            )
            (activity as AppCompatActivity).setSupportActionBar(toolbar)
            navView.setNavigationItemSelectedListener(this)
            drawerLayout.addDrawerListener(toggle)
            toggle.syncState()
            tabLayout = binding.tabLayout
            viewPager2 = binding.viewPager2
            setupObserver()
        } catch (e: Exception) {
            Logger.logE("HomeFragment: Lỗi: ${e.message.toString()}")
        }
        return binding.root
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                Toast.makeText(requireContext(), "Trang chủ", Toast.LENGTH_SHORT).show()
            }

            R.id.nav_category -> {
                Toast.makeText(requireContext(), "Danh mục", Toast.LENGTH_SHORT).show()
            }

            R.id.nav_post -> {
                Toast.makeText(requireContext(), "Đăng tin tức", Toast.LENGTH_SHORT).show()
            }

            R.id.nav_approve -> {
                Toast.makeText(requireContext(), "Phê duyệt tin tức", Toast.LENGTH_SHORT)
                    .show()
            }

            R.id.nav_role -> {
                Toast.makeText(requireContext(), "Phân quyền", Toast.LENGTH_SHORT).show()
            }

            R.id.nav_logout -> {
                Toast.makeText(requireContext(), "Đăng xuất", Toast.LENGTH_SHORT).show()
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun setupObserver() {
        viewModel.listNews.observe(viewLifecycleOwner) { resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    resource.data?.let { data ->
                        listCategories = data.result.map { it.description }
                        initTabLayoutAndViewPager()
                    }
                }

                Status.ERROR -> {
                    val error = resource.message ?: "Unknown error"
                    Logger.logE(error)
                }

                Status.LOADING -> {
                    Logger.logI("Loading...")
                }
            }
        }
    }

    private fun initTabLayoutAndViewPager() {
        val fragmentManager: FragmentManager = childFragmentManager
        adapter = ViewPagerAdapter(fragmentManager, lifecycle, listCategories)
        viewPager2.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            tab.text =
                (if (position < listCategories.size) listCategories[position] else getString(R.string.pho_bien))
        }.attach()
    }

}