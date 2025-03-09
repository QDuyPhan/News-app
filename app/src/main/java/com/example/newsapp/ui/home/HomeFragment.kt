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
import com.example.newsapp.R
import com.example.newsapp.adapter.ViewPagerAdapter
import com.example.newsapp.data.response.CategoryResponse
import com.example.newsapp.databinding.FragmentHomeBinding
import com.example.newsapp.utils.Logger
import com.example.newsapp.utils.Status
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(), NavigationView.OnNavigationItemSelectedListener {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ViewPagerAdapter
    private var listCategory: List<CategoryResponse>? = null
    private val viewModel by viewModels<HomeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        binding.homeFragment.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
        val toggle = ActionBarDrawerToggle(
            requireActivity(), binding.homeFragment, binding.toolbar,
            R.string.open_nav, R.string.close_nav
        )
        binding.homeFragment.addDrawerListener(toggle)
        toggle.syncState()
        onItemClickMenu()
        setupObserver()
    }

    private fun setupObserver() {
        viewModel.listCategory.observe(viewLifecycleOwner) { resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    resource.data?.let { data ->
                        listCategory = data.result
                    }
                    val fragmentManager: FragmentManager = childFragmentManager
                    adapter = ViewPagerAdapter(fragmentManager, lifecycle, listCategory)
                    binding.viewPager2.adapter = adapter

                    TabLayoutMediator(binding.tabLayout, binding.viewPager2) { tab, position ->
                        tab.text =
                            (if (position < listCategory?.size!!) listCategory?.get(position)?.description else null)

                    }.attach()
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

    private fun onItemClickMenu() {
        binding.navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    Toast.makeText(requireContext(), "Trang chủ", Toast.LENGTH_SHORT).show()
                }

                R.id.nav_logout -> {
                    Toast.makeText(requireContext(), "Đăng xuất", Toast.LENGTH_SHORT).show()
                }
            }
            binding.homeFragment.closeDrawer(GravityCompat.START)
            true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
                Toast.makeText(requireContext(), "Phê duyệt tin tức", Toast.LENGTH_SHORT).show()
            }

            R.id.nav_role -> {
                Toast.makeText(requireContext(), "Phân quyền", Toast.LENGTH_SHORT).show()
            }

            R.id.nav_logout -> {
                Toast.makeText(requireContext(), "Đăng xuất", Toast.LENGTH_SHORT).show()
            }
        }
        binding.homeFragment.closeDrawer(GravityCompat.START)
        return true
    }
}