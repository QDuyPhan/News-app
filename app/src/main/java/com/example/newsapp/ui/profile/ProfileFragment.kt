package com.example.newsapp.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.newsapp.R
import com.example.newsapp.databinding.DialogProfileUserBinding
import com.example.newsapp.databinding.FragmentProfileBinding
import com.example.newsapp.ui.account.AccountViewModel
import com.example.newsapp.ui.account.TokenViewModel
import com.example.newsapp.ui.base.BaseFragment
import com.example.newsapp.ui.home.HomeViewModel
import com.example.newsapp.ui.saved.SavedViewModel
import com.example.newsapp.ui.updateuserinfo.UpdateUserViewModel
import com.example.newsapp.utils.Logger
import com.example.newsapp.utils.setOnSingClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>() {
    private val profileViewModel by viewModels<ProfileViewModel>()
    private val savedViewModel by activityViewModels<SavedViewModel>()
    private val homeViewModel by viewModels<HomeViewModel>()
    private val tokenViewModel by activityViewModels<TokenViewModel>()
    private val accountViewModel by activityViewModels<AccountViewModel>()
    private val updateUserViewModel by viewModels<UpdateUserViewModel>()
    private lateinit var navController: NavController
    private var token: String? = null
    private lateinit var dialog: AlertDialog
    private var userId: String? = null

    override fun inflateBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ): FragmentProfileBinding {
        return FragmentProfileBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
        getUserId()
        checkToken()
        showUserInfo()
        showOptionDialog()
        showDialogWeather()
        logout()
        observeDeleteUser()
    }

    private fun getUserId() {
        updateUserViewModel.userId.observe(viewLifecycleOwner) {
            this.userId = it.id
        }
    }

    private fun showUserInfo() {
        with(savedViewModel) {
            user.observe(viewLifecycleOwner) { user ->
                binding.txtName.text = user?.name
                binding.txtEmail.text = user?.email
            }
        }
    }

    private fun showOptionDialog() {
        binding.apply {
            btnProfile.setOnSingClickListener {
                Toast.makeText(requireContext(), "Profile", Toast.LENGTH_SHORT).show()
                val build = AlertDialog.Builder(requireContext())
                val dialogBinding =
                    DialogProfileUserBinding.inflate(LayoutInflater.from(requireContext()))
                build.setView(dialogBinding.root)


//                val btnChangePassword = dialogBinding.btnChangePassword
                val btnUpdateInfo = dialogBinding.btnUpdateUserInfo
                val btnDeleteInfo = dialogBinding.btnDelete
//                btnChangePassword.setOnClickListener {
//                    dialog.dismiss()
//                    findNavController().navigate(R.id.action_profileFragment_to_changePasswordFragment)
//                }
                btnUpdateInfo.setOnClickListener {
                    dialog.dismiss()
                    findNavController().navigate(R.id.action_profileFragment_to_updateUserFragment)
                }
                btnDeleteInfo.setOnClickListener {
                    AlertDialog.Builder(requireContext())
                        .setTitle("Xác nhận xoá tài khoản")
                        .setMessage("Bạn có chắc chắn muốn xoá tài khoản không? Hành động này không thể hoàn tác.")
                        .setPositiveButton("Yes") { dialogInterface, _ ->
                            Logger.logI("userId: $userId")
                            accountViewModel.deleteUser(userId.toString())
                            dialogInterface.dismiss()
                        }
                        .setNegativeButton("Cancel") { dialogInterface, _ ->
                            dialogInterface.dismiss()
                        }
                        .show()
                }
                dialog = build.create()
                dialog.show()
            }
        }
    }

    private fun observeDeleteUser() {
        observeResource(
            liveData = accountViewModel.deleteUserResult,
            onSuccess = {
                logout()
            }, onError = {
                val error = it
                Logger.logE(error)
            }, onLoading = {
                Logger.logI("Waiting delete user...")
            }
        )
    }

    private fun showDialogWeather() {
        binding.apply {
            weather.setOnSingClickListener {
                Toast.makeText(requireContext(), "Weather", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun logout() {
        binding.apply {
            logout.setOnSingClickListener {
                accountViewModel.saveLoginState(false)
                accountViewModel.logout(token.toString())
                homeViewModel.deleteUserInfo()
                tokenViewModel.deleteToken()
                navController.navigate(
                    R.id.action_profileFragment_to_loginFragment,
                    null,
                    NavOptions.Builder().setPopUpTo(R.id.profileFragment, true).build()
                )
            }
        }
    }

    private fun checkToken() {
        tokenViewModel.token.observe(viewLifecycleOwner) { token ->
            this.token = token
        }
    }

}