package com.example.newsapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.newsapp.R
import com.example.newsapp.adapter.ViewPagerAdapter
import com.example.newsapp.data.remote.response.CategoryResponse
import com.example.newsapp.databinding.FragmentHomeBinding
import com.example.newsapp.ui.account.AccountViewModel
import com.example.newsapp.ui.account.TokenViewModel
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
    private val homeViewModel by viewModels<HomeViewModel>()
    private val tokenViewModel by activityViewModels<TokenViewModel>()
    private val accountViewModel by activityViewModels<AccountViewModel>()
    private lateinit var navController: NavController
    private lateinit var navigationView: NavigationView
    private var token: String? = null
    private lateinit var txtUsername: TextView
    private lateinit var txtEmail: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val headerView = binding.navView.getHeaderView(0)
        txtUsername = headerView.findViewById<TextView>(R.id.tvUsername)
        txtEmail = headerView.findViewById<TextView>(R.id.tvEmail)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigationView = binding.navView
        checkToken()
        setUpMemu()
        setupObserver()
        getUserInfo()
    }

    private fun checkToken() {
        tokenViewModel.token.observe(viewLifecycleOwner) { token ->
            this.token = token
            if (token == null)
                navController.navigate(
                    R.id.loginFragment,
                    null,
                    NavOptions.Builder().setPopUpTo(R.id.homeFragment, true).build()
                )
        }
    }

    private fun getUserInfo() {
        homeViewModel.userInfo.observe(viewLifecycleOwner) { resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    resource.data?.let {
                        homeViewModel.saveUserInfo(it.result)
                        txtUsername.text = it.result.username
                        txtEmail.text = it.result.email
                        it.result.roles.map {
                            setMenuByRole(it.name)
                        }
                    }
                }

                Status.ERROR -> {
                    val error = resource.message ?: "Unknown error"
                    Logger.logE(error)
                }

                Status.LOADING -> {
                    Logger.logI("Waiting get user info...")
                }
            }

        }
    }

    private fun setMenuByRole(role: String) {
        val isAdmin = role.equals("ADMIN", ignoreCase = true)
        val isUser = role.equals("USER", ignoreCase = true)
        val menu = binding.navView.menu
        menu.findItem(R.id.nav_approve).isVisible = isAdmin
        menu.findItem(R.id.nav_role).isVisible = isAdmin
    }


    private fun setupObserver() {
        homeViewModel.listCategory.observe(viewLifecycleOwner) { resource ->
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

    private fun setUpMemu() {
        binding.apply {
            (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
            homeFragment.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            (activity as AppCompatActivity).setSupportActionBar(toolbar)
            navigationView.setNavigationItemSelectedListener(this@HomeFragment)
            navController =
                (requireActivity().supportFragmentManager
                    .findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController

            val toggle = ActionBarDrawerToggle(
                requireActivity(), homeFragment, toolbar, R.string.open_nav, R.string.close_nav
            )
            homeFragment.addDrawerListener(toggle)
            toggle.syncState()


        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                findNavController().navigate(R.id.homeFragment)
                Logger.logI("Trang chủ")
                return true
            }

            R.id.nav_category -> {
                Toast.makeText(requireContext(), "Danh mục", Toast.LENGTH_SHORT).show()
                return true
            }

            R.id.nav_post -> {
                Toast.makeText(requireContext(), "Đăng tin tức", Toast.LENGTH_SHORT).show()
                return true
            }

            R.id.nav_approve -> {
                Toast.makeText(requireContext(), "Phê duyệt tin tức", Toast.LENGTH_SHORT).show()
                return true
            }

            R.id.nav_role -> {
                Toast.makeText(requireContext(), "Phân quyền", Toast.LENGTH_SHORT).show()
                return true
            }

            R.id.nav_logout -> {
                Toast.makeText(requireContext(), "Đăng xuất", Toast.LENGTH_SHORT).show()
                accountViewModel.saveLoginState(false)
                accountViewModel.logout(token.toString())
                homeViewModel.deleteUserInfo()
                tokenViewModel.deleteToken()
                navController.navigate(
                    R.id.action_homeFragment_to_loginFragment,
                    null,
                    NavOptions.Builder().setPopUpTo(R.id.homeFragment, true).build()
                )
                return true
            }
        }
        binding.homeFragment.closeDrawer(GravityCompat.START)
        return true
    }
}