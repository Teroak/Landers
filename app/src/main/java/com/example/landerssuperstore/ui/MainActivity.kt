package com.example.landerssuperstore.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.landerssuperstore.R
import com.example.landerssuperstore.databinding.ActivityMainBinding
import com.example.landerssuperstore.ui.cart.CartActivity
import com.example.landerssuperstore.ui.home.HomeFragment
import com.example.landerssuperstore.ui.categories.CategoriesFragment
import com.example.landerssuperstore.ui.orders.OrdersFragment
import com.example.landerssuperstore.ui.account.AccountFragment
import com.example.landerssuperstore.ui.bea_member.BeaMemberFragment
import com.example.landerssuperstore.utils.MembershipManager

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userName = intent.getStringExtra("USER_NAME") ?: "User"
        val userEmail = intent.getStringExtra("USER_EMAIL") ?: "user@landers.ph"
        val navigateToMembership = intent.getBooleanExtra("NAVIGATE_TO_MEMBERSHIP", false)

        if (savedInstanceState == null) {
            if (navigateToMembership) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, BeaMemberFragment())
                    .commit()
            } else {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, HomeFragment())
                    .commit()
            }
        }

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            val fragment = when (item.itemId) {
                R.id.nav_home -> HomeFragment()
                R.id.nav_orders -> OrdersFragment()
                R.id.nav_categories -> CategoriesFragment()
                R.id.nav_bea_member -> BeaMemberFragment()
                R.id.nav_account -> AccountFragment.newInstance(userName, userEmail)
                else -> HomeFragment()
            }
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit()
            true
        }
        
        // Update membership status in bottom navigation
        MembershipManager.updateFromFirestore()
        
        // One-time database seeding for demo purposes
        // com.example.landerssuperstore.utils.FirebaseInitializer.seedDatabase { success ->
        //     if (success) android.util.Log.d("MainActivity", "Seeded products")
        // }

        MembershipManager.isMember.observe(this) { isMember ->
            if (isMember) {
                // Change "Be a Member" to "My Membership" or similar
                val menu = binding.bottomNavigation.menu
                val membershipItem = menu.findItem(R.id.nav_bea_member)
                membershipItem?.title = "Member"
            }
        }
    }
}
