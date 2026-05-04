package com.example.landerssuperstore.ui.orders

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.landerssuperstore.R
import com.example.landerssuperstore.databinding.FragmentOrdersBinding
import com.example.landerssuperstore.ui.adapters.OrderAdapter
import com.example.landerssuperstore.ui.productlist.ProductListActivity
import com.example.landerssuperstore.utils.OrderManager

class OrdersFragment : Fragment() {

    private var _binding: FragmentOrdersBinding? = null
    private val binding get() = _binding!!
    private lateinit var orderAdapter: OrderAdapter
    private var currentTab = "All"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentOrdersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        orderAdapter = OrderAdapter(emptyList())
        binding.recyclerOrders.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerOrders.adapter = orderAdapter

        binding.buttonShopNow.setOnClickListener {
            startActivity(Intent(requireContext(), ProductListActivity::class.java))
        }

        setupTabs()

        OrderManager.orders.observe(viewLifecycleOwner) { _ ->
            refreshOrders()
        }
    }

    private fun setupTabs() {
        val tabs = listOf(
            binding.tabsContainer.getChildAt(0) as TextView to "All",
            binding.tabsContainer.getChildAt(1) as TextView to "To Ship",
            binding.tabsContainer.getChildAt(2) as TextView to "To Receive",
            binding.tabsContainer.getChildAt(3) as TextView to "To Rate"
        )

        tabs.forEach { (textView, status) ->
            textView.setOnClickListener {
                currentTab = status
                updateTabStyles(tabs)
                refreshOrders()
            }
        }
        updateTabStyles(tabs)
    }

    private fun updateTabStyles(tabs: List<Pair<TextView, String>>) {
        val primaryColor = ContextCompat.getColor(requireContext(), R.color.primary)
        val grayColor = ContextCompat.getColor(requireContext(), R.color.gray_medium)
        tabs.forEach { (textView, status) ->
            if (status == currentTab) {
                textView.setTextColor(primaryColor)
                textView.setTypeface(null, android.graphics.Typeface.BOLD)
            } else {
                textView.setTextColor(grayColor)
                textView.setTypeface(null, android.graphics.Typeface.NORMAL)
            }
        }
    }

    private fun refreshOrders() {
        val orders = OrderManager.getOrdersByStatus(currentTab)
        orderAdapter.updateList(orders)
        if (orders.isEmpty()) {
            binding.layoutEmpty.visibility = View.VISIBLE
            binding.recyclerOrders.visibility = View.GONE
        } else {
            binding.layoutEmpty.visibility = View.GONE
            binding.recyclerOrders.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
