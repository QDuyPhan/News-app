package com.example.newsapp.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.newsapp.R
import com.example.newsapp.databinding.CustomDialogBinding
import com.example.newsapp.databinding.FragmentRegisterBinding
import com.example.newsapp.ui.base.BaseFragment
import com.example.newsapp.ui.widget.CustomToast
import com.example.newsapp.utils.Logger
import com.example.newsapp.utils.setOnSingClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : BaseFragment<FragmentRegisterBinding>() {
    private lateinit var dialog: AlertDialog
    private val viewModel by viewModels<AccountViewModel>()

    override fun inflateBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ): FragmentRegisterBinding {
        return FragmentRegisterBinding.inflate(inflater, container, false)
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

                if (name.isBlank() || email.isBlank() || password.isBlank() || username.isBlank()) {
                    if (name.isBlank()) CustomToast.makeText(
                        requireContext(),
                        "Tên không được bỏ trống",
                        CustomToast.LONG_DURATION,
                        CustomToast.WARNING,
                        R.drawable.warning_icon
                    ).show()
                    if (email.isBlank()) CustomToast.makeText(
                        requireContext(),
                        "Email không được bỏ trống",
                        CustomToast.LONG_DURATION,
                        CustomToast.WARNING,
                        R.drawable.warning_icon
                    ).show()
                    if (password.isBlank()) CustomToast.makeText(
                        requireContext(),
                        "Mật khẩu không đươc bỏ trống",
                        CustomToast.LONG_DURATION,
                        CustomToast.WARNING,
                        R.drawable.warning_icon
                    ).show()
                    if (username.isBlank()) CustomToast.makeText(
                        requireContext(),
                        "Username không được bỏ trống",
                        CustomToast.LONG_DURATION,
                        CustomToast.WARNING,
                        R.drawable.warning_icon
                    ).show()
                } else {
                    viewModel.signup(name, username, password, email)
                    setupObserver()
                }
            }
            txtLogin.setOnSingClickListener {
                findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
            }
        }

    }

    private fun clearEmptyText() {
        binding.apply {
            editNameSignUp.text.clear()
            editUsernameSignUp.text.clear()
            editPassSignUp.text.clear()
            editEmailSignUp.text.clear()
        }
    }

    private fun setupObserver() {
        binding.apply {
            observeResource(liveData = viewModel.signupResult, onSuccess = {
                prgBarMovies.visibility = View.GONE
                clearEmptyText()
                CustomToast.makeText(
                    requireContext(),
                    "Đăng Ký Thành Công",
                    CustomToast.LONG_DURATION,
                    CustomToast.SUCCESS,
                    R.drawable.check_icon
                ).show()
            }, onError = {
                prgBarMovies.visibility = View.GONE
                val error = it
                CustomToast.makeText(
                    requireContext(),
                    error,
                    CustomToast.LONG_DURATION,
                    CustomToast.SUCCESS,
                    R.drawable.check_icon
                ).show()
                Logger.logE(error)
            }, onLoading = {
                prgBarMovies.visibility = View.VISIBLE
            })
        }
    }

    private fun showAlertDialog() {
        val build = AlertDialog.Builder(requireContext())
        val dialogBinding = CustomDialogBinding.inflate(LayoutInflater.from(requireContext()))
        build.setView(dialogBinding.root)
        dialogBinding.txtDialog.text = "Đăng Ký Thành Công"
        dialogBinding.btnDialog.setOnSingClickListener {
            dialog.dismiss()
        }
        dialog = build.create()
        dialog.show()
    }
}