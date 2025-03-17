package com.example.newsapp.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentLoginBinding
import com.example.newsapp.utils.Logger
import com.example.newsapp.utils.Status
import com.example.newsapp.utils.setOnSingClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<AccountViewModel>()
    private val tokenViewModel by activityViewModels<TokenViewModel>()
    private lateinit var navController: NavController


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
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

    private fun checkToken() {
        tokenViewModel.token.observe(viewLifecycleOwner) { token ->
            if (token != null)
                navController.navigate(
                    R.id.action_loginFragment_to_homeFragment,
                    null,
                    NavOptions.Builder().setPopUpTo(R.id.loginFragment, true).build()
                )
        }
    }

    private fun setupObserver() {
        viewModel.loginResult.observe(viewLifecycleOwner) { resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    viewModel.saveLoginState(true)
                    resource.data?.let {
                        tokenViewModel.saveToken(it.result.token)
                    }
                    checkToken()
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