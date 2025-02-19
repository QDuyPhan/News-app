package com.example.newsapp.ui.account

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.newsapp.databinding.FragmentRegisterBinding
import com.example.newsapp.utils.Logger
import com.example.newsapp.utils.Resource
import com.example.newsapp.utils.Status
import com.example.newsapp.utils.setOnSingClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private val viewModel by viewModels<RegisterViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClickListener()
    }

    private fun onClickListener() {
        binding.apply {
            signUpBtn.setOnSingClickListener {
                val name = editNameSignUp.text.toString()
                val username = editUsernameSignUp.text.toString()
                val password = editPassSignUp.text.toString()
                val email = editEmailSignUp.text.toString()
                if (username.isEmpty() || name.isEmpty() || password.isEmpty() || email.isEmpty()) {
                    Logger.logW("Hãy Nhập Đầy Đủ Thông Tin")
                } else {
                    viewModel.signup(name, username, password, email)
                    setupObserver()
                }
            }
        }

    }

    private fun setupObserver() {
        viewModel.signupResult.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    Logger.logI("Register Successfully")
                }

                Status.ERROR -> {
                    val error = it.message ?: "Unknown error"
                    Logger.logE(error)
                }

                Status.LOADING -> {
                    Logger.logI("Loading...")
                }
            }

        }
    }
}