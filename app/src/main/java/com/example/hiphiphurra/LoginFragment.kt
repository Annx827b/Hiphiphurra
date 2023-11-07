package com.example.hiphiphurra

import android.os.Bundle
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hiphiphurra.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private var base: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val currentUser = base.currentUser
        if (currentUser != null) {
            findNavController().navigate(R.id.action_loginFragment_to_friendsListFragment)
        }
        binding.loginButton.setOnClickListener {
            val email = binding.editEmail.text.trim().toString()
            if (email.isEmpty()) {
                binding.editEmail.error = "Email = no"
                return@setOnClickListener
            }
            val password = binding.editPassword.text.trim().toString()
            if (password.isEmpty()) {
                binding.editPassword.error = "Password = no"
                return@setOnClickListener
            }
            base.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        Log.d("APPLE", "Email:success")

                        val user = base.currentUser
                        findNavController().navigate(R.id.action_loginFragment_to_friendsListFragment)
                    } else {
                        Log.w("APPLE", "Email:failure", task.exception)
                        binding.viewMessage.text =
                            "Failed: - Not a user.  " + task.exception?.message
                    }
                }
        }

        binding.registerButton.setOnClickListener {
            val email = binding.editEmail.text.trim().toString()
            if (email.isEmpty()) {
                binding.editEmail.error = "Email = no"
                return@setOnClickListener
            }
            val password = binding.editPassword.text.trim().toString()
            if (password.isEmpty()) {
                binding.editPassword.error = "Password = no"
                return@setOnClickListener
            }
            base.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        Log.d("APPLE", "Email:success")
                        val user = base.currentUser
                        findNavController().navigate(R.id.action_loginFragment_to_friendsListFragment)
                    } else {
                        Log.w("APPLE", "Email:failure", task.exception)
                        binding.viewMessage.text =
                            "Failed:  - Already a user.   " + task.exception?.message
                    }
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }
}