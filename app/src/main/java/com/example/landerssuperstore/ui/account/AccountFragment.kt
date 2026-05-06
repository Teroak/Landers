package com.example.landerssuperstore.ui.account

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.landerssuperstore.databinding.FragmentAccountBinding
import com.example.landerssuperstore.ui.login.LoginActivity
import com.example.landerssuperstore.utils.MembershipManager
import com.example.landerssuperstore.utils.AddressManager

class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!

    private var userName: String = "User"
    private var userEmail: String = "user@landers.ph"

    companion object {
        const val REQUEST_EDIT_PROFILE = 1001

        fun newInstance(name: String, email: String): AccountFragment {
            val fragment = AccountFragment()
            val args = Bundle()
            args.putString("USER_NAME", name)
            args.putString("USER_EMAIL", email)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userName = arguments?.getString("USER_NAME") ?: "User"
        userEmail = arguments?.getString("USER_EMAIL") ?: "user@landers.ph"

        binding.textProfileName.text = userName
        binding.textProfileEmail.text = userEmail

        // Update membership status indicator
        updateMembershipStatus()

        binding.buttonLogout.setOnClickListener {
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        binding.rowMembership.setOnClickListener {
            if (MembershipManager.isUserMember()) {
                // If already a member, show membership details
                Toast.makeText(requireContext(), "You are already a Landers member!", Toast.LENGTH_SHORT).show()
            } else {
                // If not a member, navigate to membership screen
                val intent = Intent(requireContext(), com.example.landerssuperstore.ui.MainActivity::class.java)
                intent.putExtra("NAVIGATE_TO_MEMBERSHIP", true)
                startActivity(intent)
            }
        }

        binding.rowPayment.setOnClickListener {
            Toast.makeText(requireContext(), "Payment Methods", Toast.LENGTH_SHORT).show()
        }

        binding.rowAddress.setOnClickListener {
            showAddressDialog()
        }

        binding.rowHelp.setOnClickListener {
            Toast.makeText(requireContext(), "Help Center", Toast.LENGTH_SHORT).show()
        }

        binding.rowChangePassword.setOnClickListener {
            startActivity(Intent(requireContext(), ChangePasswordActivity::class.java))
        }
    }

    private fun updateMembershipStatus() {
        val isMember = MembershipManager.isUserMember()
        
        // Update the membership row text and icon
        if (isMember) {
            binding.rowMembership.text = "&#127909; My Membership (Active)"
            // Add visual indicator - could be a badge or different color
            binding.rowMembership.setTextColor(getColorFromResource(com.example.landerssuperstore.R.color.primary))
        } else {
            binding.rowMembership.text = "&#127909; Join Membership"
            binding.rowMembership.setTextColor(getColorFromResource(com.example.landerssuperstore.R.color.text_primary))
        }
    }

    private fun showAddressDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Address Book")
        builder.setMessage("Enter your delivery address:")

        val input = EditText(requireContext())
        input.setText(AddressManager.getAddress() ?: "")
        val lp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        input.layoutParams = lp
        builder.setView(input)

        builder.setPositiveButton("Save") { dialog, _ ->
            val address = input.text.toString().trim()
            if (address.isNotEmpty()) {
                AddressManager.setAddress(address)
                Toast.makeText(requireContext(), "Address updated", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Address cannot be empty", Toast.LENGTH_SHORT).show()
            }
            dialog.dismiss()
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }

    private fun getColorFromResource(colorResId: Int): Int {
        return ContextCompat.getColor(requireContext(), colorResId)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_EDIT_PROFILE && resultCode == Activity.RESULT_OK) {
            val newName = data?.getStringExtra("NEW_NAME")
            val newEmail = data?.getStringExtra("NEW_EMAIL")
            if (newName != null) {
                userName = newName
                binding.textProfileName.text = newName
            }
            if (newEmail != null) {
                userEmail = newEmail
                binding.textProfileEmail.text = newEmail
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
