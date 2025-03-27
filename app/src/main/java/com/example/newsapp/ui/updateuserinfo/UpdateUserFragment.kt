package com.example.newsapp.ui.updateuserinfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentUpdateUserBinding
import com.example.newsapp.ui.base.BaseFragment
import com.example.newsapp.ui.widget.CustomToast
import com.example.newsapp.utils.Logger
import com.example.newsapp.utils.setOnSingClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpdateUserFragment : BaseFragment<FragmentUpdateUserBinding>() {
    private val updateUserViewModel by viewModels<UpdateUserViewModel>()
    private var userId: String? = null

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentUpdateUserBinding {
        return FragmentUpdateUserBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getUserId()
        initUI()
        updateUserInfo()
    }

    private fun initUI() {
        binding.apply {
            btnUpdateUserInfo.setOnSingClickListener {
                val name = edtName.text.toString()
                val email = edtEmail.text.toString()
                val password = edtPass.text.toString()
                updateUserViewModel.updateUser(userId.toString(), name, email, password)

            }
        }
    }

    private fun getUserId() {
        updateUserViewModel.userId.observe(viewLifecycleOwner) {
            this.userId = it.id
        }
    }

    private fun clearEditText() {
        binding.apply {
            edtName.text.clear()
            edtEmail.text.clear()
            edtPass.text.clear()
        }
    }

    private fun updateUserInfo() {
        observeResource(
            liveData = updateUserViewModel.updateUserResult,
            onSuccess = {
                CustomToast.makeText(
                    requireContext(),
                    "Cập nhật thành công",
                    CustomToast.LONG_DURATION,
                    CustomToast.SUCCESS,
                    R.drawable.check_icon
                ).show()
                updateUserViewModel.saveUserInfo(it.result)
                clearEditText()
            }, onError = {
                CustomToast.makeText(
                    requireContext(),
                    "Cập nhật thành công",
                    CustomToast.LONG_DURATION,
                    CustomToast.ERROR,
                    R.drawable.error_icon
                ).show()
                Logger.logI("Error update user info: $it")
                clearEditText()
            }, onLoading = {}
        )

    }
}