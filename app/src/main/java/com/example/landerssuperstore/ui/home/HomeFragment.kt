package com.example.landerssuperstore.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.landerssuperstore.data.model.Product
import com.example.landerssuperstore.data.repository.ProductRepository
import com.example.landerssuperstore.databinding.FragmentHomeBinding
import com.example.landerssuperstore.ui.adapters.ProductAdapter
import com.example.landerssuperstore.ui.productdetails.ProductDetailsActivity
import com.example.landerssuperstore.ui.search.SearchActivity
import com.example.landerssuperstore.utils.CartManager

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchBar.setOnClickListener {
            startActivity(Intent(requireContext(), SearchActivity::class.java))
        }

        binding.buttonShopNow.setOnClickListener {
            val intent = Intent(requireContext(), com.example.landerssuperstore.ui.productlist.ProductListActivity::class.java)
            intent.putExtra("CATEGORY_NAME", "NEW! Marketplace")
            startActivity(intent)
        }

        val featuredProducts = ProductRepository.getFeaturedProducts()
        binding.recyclerFeatured.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerFeatured.adapter = ProductAdapter(featuredProducts, ::onProductClick, ::onAddToCart)

        val deals = ProductRepository.getDeals()
        binding.recyclerDeals.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerDeals.adapter = ProductAdapter(deals, ::onProductClick, ::onAddToCart)
    }

    private fun onProductClick(product: Product) {
        val intent = Intent(requireContext(), ProductDetailsActivity::class.java)
        intent.putExtra("PRODUCT", product)
        startActivity(intent)
    }

    private fun onAddToCart(product: Product) {
        CartManager.addToCart(product)
        Toast.makeText(requireContext(), "${product.name} added to cart", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
