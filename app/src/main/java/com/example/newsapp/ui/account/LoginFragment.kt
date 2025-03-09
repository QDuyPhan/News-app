package com.example.newsapp.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentLoginBinding
import com.example.newsapp.utils.Constants.ACTIVATE
import com.example.newsapp.utils.Logger
import com.example.newsapp.utils.Status
import com.example.newsapp.utils.setOnSingClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<AccountViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClickListener()
        loginWithGoogle()
        loginWithFacebook()
    }

    private fun onClickListener() {
        binding.apply {
            signInBtn.setOnSingClickListener {
                val username = editUsernameSignIN.text.toString()
                val password = editUsernameSignIN.text.toString()
                if (username.isEmpty() || password.isEmpty()) {
                    Logger.logI("Hãy Nhập Đầy Đủ Thông Tin")
                } else {
                    viewModel.login(username, password)
                    setupObserver()
                }
            }
            txtSignup.setOnSingClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }
        }
    }

    private fun setupObserver() {
        viewModel.loginResult.observe(viewLifecycleOwner) { resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    viewModel.saveLoginState(ACTIVATE)
                    Logger.logI("LoginFragment isLogin: ${viewModel.isLogin()}")
                    resource.data?.let {
                        viewModel.saveToken(it.result.token)
                    }
                    val navController = findNavController()
                    navController.navigate(R.id.action_loginFragment_to_homeFragment)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}