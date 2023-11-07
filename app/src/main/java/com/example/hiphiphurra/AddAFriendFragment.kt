package com.example.hiphiphurra

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.hiphiphurra.databinding.AddAFriendFragmentBinding
import com.example.hiphiphurra.models.Friend
import com.example.hiphiphurra.models.FriendsViewModel
import com.google.firebase.auth.FirebaseAuth
import java.util.*

class FriendFragment : Fragment() {
    private var _binding: AddAFriendFragmentBinding? = null
    private val binding get() = _binding!!

    private val friendsViewModel: FriendsViewModel by activityViewModels()

    // Assuming you have Firebase set up in your project.
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AddAFriendFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonPickDate.setOnClickListener {
            showDatePickerDialog()
        }
        binding.buttonAdd.setOnClickListener {
            val name = binding.editTextName.text.trim().toString()

            if (name.isEmpty()) {
                binding.editTextName.error = "Please enter a name."
            } else {
                val newFriend = Friend(auth.currentUser?.email ?: "", name)
                friendsViewModel.add(newFriend)
                findNavController().popBackStack()
            }
        }

        binding.buttonBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
            // Format the selected date and set the text view to show the selected date
            binding.textViewSelectedDate.text = String.format(
                Locale.getDefault(),
                "%d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay
            )
        }, year, month, dayOfMonth).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
