package com.example.newsapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
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
import com.example.newsapp.databinding.DialogUserinfoBinding
import com.example.newsapp.databinding.FragmentHomeBinding
import com.example.newsapp.ui.account.AccountViewModel
import com.example.newsapp.ui.account.TokenViewModel
import com.example.newsapp.ui.base.BaseFragment
import com.example.newsapp.ui.saved.SavedViewModel
import com.example.newsapp.utils.Logger
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(),
    NavigationView.OnNavigationItemSelectedListener {
    private lateinit var adapter: ViewPagerAdapter
    private var listCategory: List<CategoryResponse>? = null
    private val homeViewModel by viewModels<HomeViewModel>()
    private val tokenViewModel by activityViewModels<TokenViewModel>()
    private val accountViewModel by activityViewModels<AccountViewModel>()
    private val savedViewModel by activityViewModels<SavedViewModel>()
    private lateinit var navController: NavController
    private lateinit var navigationView: NavigationView
    private var token: String? = null
    private lateinit var dialog: AlertDialog

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val headerView = binding.navView.getHeaderView(0)
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
        observeResource(
            liveData = homeViewModel.userInfo,
            onSuccess = {
                homeViewModel.saveUserInfo(it.result)
                it.result.roles.map {
                    setMenuByRole(it.name)
                }
            },
            onError = {
                val error = it
                Logger.logE(error)
            },
            onLoading = {
                Logger.logI("Waiting get user info...")
            }
        )
    }

    private fun setMenuByRole(role: String) {
        val isAdmin = role.equals("ADMIN", ignoreCase = true)
        val isUser = role.equals("USER", ignoreCase = true)
        val menu = binding.navView.menu
        menu.findItem(R.id.nav_approve).isVisible = isAdmin
        menu.findItem(R.id.nav_role).isVisible = isAdmin
        menu.findItem(R.id.nav_role).isVisible = isAdmin
//        menu.findItem(R.id.nav_category).isVisible = isAdmin
        menu.findItem(R.id.nav_approve).isVisible = isAdmin
    }

    private fun setupObserver() {
        observeResource(
            liveData = homeViewModel.listCategory,
            onSuccess = {
                listCategory = it.result
                val fragmentManager: FragmentManager = childFragmentManager
                adapter = ViewPagerAdapter(
                    fragmentManager,
                    lifecycle,
                    listCategory,
                    previousScreen = "home"
                )
                binding.viewPager2.adapter = adapter

                TabLayoutMediator(binding.tabLayout, binding.viewPager2) { tab, position ->
                    tab.text =
                        (if (position < listCategory?.size!!) listCategory?.get(position)?.description else null)

                }.attach()
            },
            onError = {
                val error = it
                Logger.logE(error)
            },
            onLoading = {
                Logger.logI("Loading...")
            }
        )
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

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                findNavController().navigate(R.id.homeFragment)
                Logger.logI("Trang chủ")
                return true
            }

//            R.id.nav_category -> {
//                Toast.makeText(requireContext(), "Danh mục", Toast.LENGTH_SHORT).show()
//                return true
//            }

            R.id.nav_approve -> {
                findNavController().navigate(R.id.action_homeFragment_to_managePostsFragment)
                return true
            }

            R.id.nav_role -> {
                Toast.makeText(requireContext(), "Phân quyền", Toast.LENGTH_SHORT).show()
                return true
            }

            R.id.nav_user -> {
                showAlertDialog()
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

    private fun showAlertDialog() {
        val build = AlertDialog.Builder(requireContext())
        val dialogBinding = DialogUserinfoBinding.inflate(LayoutInflater.from(requireContext()))
        build.setView(dialogBinding.root)
        with(savedViewModel) {
            user.observe(viewLifecycleOwner) { user ->
                dialogBinding.apply {
                    tvId.text = user?.id.toString()
                    tvName.text = user?.name.toString()
                    tvUsername.text = user?.username.toString()
                    tvEmail.text = user?.email.toString()
                }
            }
        }
        dialog = build.create()
        dialog.show()
    }
}
