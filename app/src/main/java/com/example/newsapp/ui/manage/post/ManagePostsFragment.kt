package com.example.newsapp.ui.manage.post

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.newsapp.databinding.FragmentManagePostsBinding
import com.example.newsapp.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ManagePostsFragment : BaseFragment<FragmentManagePostsBinding>() {

    private val managePostsViewModel by viewModels<ManagePostsViewModel>()
    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentManagePostsBinding {
        return FragmentManagePostsBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


}