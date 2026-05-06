package com.example.landerssuperstore.ui.bea_member

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.landerssuperstore.R
import com.example.landerssuperstore.databinding.FragmentBeaMemberBinding
import com.example.landerssuperstore.ui.payment.MembershipPaymentActivity
import com.example.landerssuperstore.utils.MembershipManager

class BeaMemberFragment : Fragment() {

    private var _binding: FragmentBeaMemberBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentBeaMemberBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Update membership status indicator
        updateMembershipStatus()
        
        // Handle join button click
        binding.buttonJoinNow.setOnClickListener {
            // Navigate to membership payment activity
            val intent = Intent(requireContext(), MembershipPaymentActivity::class.java)
            startActivity(intent)
        }
        
        // Handle tier selection
        binding.cardTierBasic.setOnClickListener {
            // Navigate to membership payment activity for basic tier
            val intent = Intent(requireContext(), MembershipPaymentActivity::class.java)
            intent.putExtra("MEMBERSHIP_TIER", "basic")
            startActivity(intent)
        }
        
        binding.cardTierPremium.setOnClickListener {
            // Navigate to membership payment activity for premium tier
            val intent = Intent(requireContext(), MembershipPaymentActivity::class.java)
            intent.putExtra("MEMBERSHIP_TIER", "premium")
            startActivity(intent)
        }
        
        // Set membership status to true when user joins
        // This would be handled by the registration flow in a real implementation
    }

    private fun updateMembershipStatus() {
        val isMember = MembershipManager.isUserMember()
        
        if (isMember) {
            // Show membership confirmation
            binding.textMembershipTitle.text = "Welcome Member!"
            binding.textMembershipSubtitle.text = "Thank you for joining Landers Superstore Membership. Enjoy exclusive benefits!"
            
            // Update join button text
            binding.buttonJoinNow.text = "VIEW MEMBERSHIP DETAILS"
            
            // Hide tier cards or show different content
            binding.cardTierBasic.visibility = android.view.View.GONE
            binding.cardTierPremium.visibility = android.view.View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}