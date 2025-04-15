package com.example.newsapp.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentLoginBinding
import com.example.newsapp.ui.base.BaseFragment
import com.example.newsapp.ui.widget.CustomToast
import com.example.newsapp.utils.Logger
import com.example.newsapp.utils.setOnSingClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>() {
    private val accountViewModel by viewModels<AccountViewModel>()
    private val tokenViewModel by activityViewModels<TokenViewModel>()
    private lateinit var navController: NavController
    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentLoginBinding {
        return FragmentLoginBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        onClickListener()
        loginWithGoogle()
        loginWithFacebook()
    }

    private fun onClickListener() {
        binding.apply {
            signInBtn.setOnSingClickListener {
                val username = editUsernameSignIN.text.toString()
                val password = editPassSignIn.text.toString()
                if (username.isBlank() || password.isBlank()) {
                    if (username.isBlank()) CustomToast.makeText(
                        requireContext(),
                        "Username không được bỏ trống",
                        CustomToast.LONG_DURATION,
                        CustomToast.WARNING,
                        R.drawable.warning_icon
                    ).show()
                    if (password.isBlank()) CustomToast.makeText(
                        requireContext(),
                        "Password không được bỏ trống",
                        CustomToast.LONG_DURATION,
                        CustomToast.WARNING,
                        R.drawable.warning_icon
                    ).show()
                } else {
                    accountViewModel.login(username, password)
                    setupObserver()
                }
            }
            txtSignup.setOnSingClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }
        }
    }

    private fun checkToken() {
        tokenViewModel.token.observe(viewLifecycleOwner) { token ->
            if (token != null)
                navController.navigate(
                    R.id.homeFragment,
                    null,
                    NavOptions.Builder().setPopUpTo(R.id.loginFragment, true).build()
                )
        }
    }

    private fun setupObserver() {
        observeResource(
            liveData = accountViewModel.loginResult,
            onSuccess = {
                binding.prgBarLogin.visibility = View.VISIBLE
                accountViewModel.saveLoginState(value = true)
                tokenViewModel.saveToken(it.result.token)
                checkToken()
                CustomToast.makeText(
                    requireContext(),
                    "Đăng Nhập Thành Công",
                    CustomToast.LONG_DURATION,
                    CustomToast.SUCCESS,
                    R.drawable.check_icon
                ).show()
                binding.prgBarLogin.visibility = View.GONE
            },
            onError = {
                binding.prgBarLogin.visibility = View.VISIBLE
                val error = it
                Logger.logE(error)
                CustomToast.makeText(
                    requireContext(),
                    "Đăng Nhập Thất Bại",
                    CustomToast.LONG_DURATION,
                    CustomToast.ERROR,
                    R.drawable.error_icon
                )
                binding.prgBarLogin.visibility = View.GONE
            },
            onLoading = {
                binding.prgBarLogin.visibility = View.VISIBLE
                Logger.logI("Đang đăng nhập...")
            }
        )
    }


    private fun loginWithGoogle() {
        val btnGoogle = binding.googleLoginBtn
        btnGoogle.setOnSingClickListener {
            Logger.logI("Login with Google")
        }
    }

    private fun loginWithFacebook() {
        val btnGoogle = binding.fbLoginBtn
        btnGoogle.setOnSingClickListener {
            Logger.logI("Login with Facebook")
        }
    }
}