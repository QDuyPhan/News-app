package com.example.newsapp.ui.account

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.newsapp.R
import com.example.newsapp.data.local.PreferenceRepository
import com.example.newsapp.databinding.FragmentLoginBinding
import com.example.newsapp.ui.main.MainActivity
import com.example.newsapp.utils.Logger
import com.example.newsapp.utils.Resource
import com.example.newsapp.utils.Status
import com.example.newsapp.utils.setOnSingClickListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private val viewModel by viewModels<LoginViewModel>()

    @Inject
    lateinit var preferenceRepository: PreferenceRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
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
                    Logger.logI("Login Successfully")
                    startActivity(Intent(requireContext(), MainActivity::class.java))
                    requireActivity().finish()
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
}