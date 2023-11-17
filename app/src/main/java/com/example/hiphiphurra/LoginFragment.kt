package com.example.hiphiphurra

import android.os.Bundle
import androidx.navigation.fragment.findNavController
import com.example.hiphiphurra.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

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
                        Log.d("Sign_In", "Email:success")

                        val user = base.currentUser
                        findNavController().navigate(R.id.action_loginFragment_to_friendsListFragment)
                    } else {
                        Log.w("Sign_In", "Email:failure", task.exception)
                        binding.viewMessage.text =
                            "Hov noget gik galt! Tjek om din mail og kodeord er skrevet korrekt!"
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
                        Log.d("Create_Log", "Email:success")
                        val user = base.currentUser
                        findNavController().navigate(R.id.action_loginFragment_to_friendsListFragment)
                    } else {
                        Log.w("Create_Log", "Email:failure", task.exception)
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