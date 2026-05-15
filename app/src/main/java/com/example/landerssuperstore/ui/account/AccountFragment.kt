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
import com.example.landerssuperstore.data.repository.MemberRepository
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
        fetchRealMemberData()

        binding.buttonLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
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

    private fun fetchRealMemberData() {
        MemberRepository.getMemberData { data ->
            if (data != null) {
                val firstName = data["Mem_FirstName"] as? String ?: userName
                val lastName = data["Mem_LastName"] as? String ?: ""
                val email = data["Mem_Email"] as? String ?: userEmail
                val type = data["Mem_Type"] as? String ?: "Premium"
                val status = data["Mem_Status"] as? String ?: "Active"
                
                // Firestore Timestamp handling (simplified for this example)
                val expiryDate = data["Mem_ExpiryDate"] as? com.google.firebase.Timestamp
                val expiryStr = if (expiryDate != null) {
                    SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(expiryDate.toDate())
                } else {
                    "N/A"
                }

                activity?.runOnUiThread {
                    binding.textProfileName.text = "$firstName $lastName"
                    binding.textProfileEmail.text = email
                    
                    if (status == "Active") {
                        binding.rowMembership.text = "Membership: $type (Expires: $expiryStr)"
                        binding.rowMembership.setTextColor(getColorFromResource(com.example.landerssuperstore.R.color.primary))
                        MembershipManager.setMembershipStatus(true)
                    } else {
                        binding.rowMembership.text = "Membership: $status"
                        binding.rowMembership.setTextColor(getColorFromResource(com.example.landerssuperstore.R.color.text_primary))
                        MembershipManager.setMembershipStatus(false)
                    }
                }
            }
        }
    }

    private fun updateMembershipStatus() {
        // Kept for backward compatibility but replaced by fetchRealMemberData
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
