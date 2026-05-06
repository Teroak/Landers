package com.example.landerssuperstore.ui.categories

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.landerssuperstore.data.repository.ProductRepository
import com.example.landerssuperstore.databinding.FragmentCategoriesBinding
import com.example.landerssuperstore.ui.adapters.CategoryAdapter
import com.example.landerssuperstore.ui.productlist.ProductListActivity
import com.example.landerssuperstore.ui.search.SearchActivity

class CategoriesFragment : Fragment() {

    private var _binding: FragmentCategoriesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchBar.setOnClickListener {
            startActivity(Intent(requireContext(), SearchActivity::class.java))
        }

        binding.buttonCart.setOnClickListener {
            startActivity(Intent(requireContext(), com.example.landerssuperstore.ui.cart.CartActivity::class.java))
        }

        val categories = ProductRepository.getCategories()
        binding.recyclerCategories.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerCategories.adapter = CategoryAdapter(categories) { category ->
            val intent = Intent(requireContext(), ProductListActivity::class.java)
            intent.putExtra("CATEGORY_NAME", category.name)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
