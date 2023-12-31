package com.example.ecommerce.fragments.categories

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommerce.R
import com.example.ecommerce.adapters.SpecialProductAdapter
import com.example.ecommerce.databinding.FragmentMainCategoryBinding
import com.example.ecommerce.util.Resource
import com.example.ecommerce.viewModels.MainCategoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

private val TAG = "MainCategoryFragment"
@AndroidEntryPoint
class MainCategoryFragment : Fragment(R.layout.fragment_main_category) {

    private lateinit var binding: FragmentMainCategoryBinding
    private lateinit var specialProductAdapter: SpecialProductAdapter
    private val viewModel by viewModels<MainCategoryViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainCategoryBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSpecialProductRv()
        lifecycleScope.launchWhenStarted {
            viewModel.specialProducts.collectLatest {
                when (it){
                    is Resource.Loading ->{
                        showLoading()
                    }
                    is Resource.Success ->{
                        specialProductAdapter.differ.submitList(it.data)
                        hideLoading()
                    }
                    is Resource.Error ->{
                        hideLoading()
                        Log.e(com.example.ecommerce.fragments.categories.TAG, it.message.toString())
                        Toast.makeText(requireContext(),it.message, Toast.LENGTH_LONG).show()
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun hideLoading() {
        binding.mainCategoryProgressbar.visibility = View.GONE
    }

    private fun showLoading() {
        binding.mainCategoryProgressbar.visibility = View.VISIBLE
    }

    private fun setupSpecialProductRv() {
        specialProductAdapter = SpecialProductAdapter()
        binding.rvSpecialProducts.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL, false)
            adapter = specialProductAdapter
        }
    }
}