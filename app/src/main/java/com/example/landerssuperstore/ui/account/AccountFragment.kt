package com.example.landerssuperstore.ui.account

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.landerssuperstore.databinding.FragmentAccountBinding
import com.example.landerssuperstore.ui.login.LoginActivity

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

        binding.buttonLogout.setOnClickListener {
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        binding.rowMembership.setOnClickListener {
            val intent = Intent(requireContext(), EditProfileActivity::class.java)
            intent.putExtra("CURRENT_NAME", userName)
            intent.putExtra("CURRENT_EMAIL", userEmail)
            startActivityForResult(intent, REQUEST_EDIT_PROFILE)
        }

        binding.rowPayment.setOnClickListener {
            Toast.makeText(requireContext(), "Payment Methods", Toast.LENGTH_SHORT).show()
        }

        binding.rowAddress.setOnClickListener {
            Toast.makeText(requireContext(), "Address Book", Toast.LENGTH_SHORT).show()
        }

        binding.rowHelp.setOnClickListener {
            Toast.makeText(requireContext(), "Help Center", Toast.LENGTH_SHORT).show()
        }

        binding.rowChangePassword.setOnClickListener {
            startActivity(Intent(requireContext(), ChangePasswordActivity::class.java))
        }
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
