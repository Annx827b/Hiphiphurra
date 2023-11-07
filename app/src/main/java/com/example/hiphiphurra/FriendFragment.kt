package com.example.hiphiphurra

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.hiphiphurra.models.Friend
import androidx.navigation.fragment.findNavController
import com.example.hiphiphurra.databinding.FragmentFriendBinding
import com.example.hiphiphurra.repository.FriendsViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import java.util.Calendar

class FriendFragment : Fragment() {
    private var _binding: FragmentFriendBinding? = null
    private val binding get() = _binding!!
    private var base: FirebaseAuth = FirebaseAuth.getInstance()
    private val viewModel: FriendsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFriendBinding.inflate(inflater, container, false)
        return binding.root
    }

    fun getBar(view: View) {
        Snackbar.make(view, "Date not valid", Snackbar.LENGTH_LONG)
            .setAction("Action", null).show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = requireArguments()
        val friendFragmentArgs: Fragment = friendFragmentArgs.fromBundle(bundle)
        val position = friendFragmentArgs.pos
        val friend = viewModel[position]

        if (friend == null) {
            binding.textMessage.text = "There were not such a friend!"
            return
        }
        binding.editTextName.setText(friend.name)
        binding.editDay.setText(friend.birthDayOfMonth.toString())
        binding.editMonth.setText(friend.birthMonth.toString())
        binding.editYear.setText(friend.birthYear.toString())
        binding.viewAge.setText("Age: " + friend.age.toString())

        binding.backButton.setOnClickListener { findNavController().popBackStack() }

        binding.deleteButton.setOnClickListener {
            viewModel.delete(friend.id)
            findNavController().popBackStack()
        }

        binding.updateButton.setOnClickListener {
            val user = base.currentUser
            val currentYear = Calendar.getInstance().get(Calendar.YEAR)
            val name = binding.editTextName.text.toString().trim()
            val year = binding.editYear.text.toString().trim().toInt()
            val month = binding.editMonth.text.toString().trim().toInt()
            val day = binding.editDay.text.toString().trim().toInt()
            val age = 1
            if (name.isEmpty()) {
                binding.editTextName.error = "Please enter a name."
                return@setOnClickListener
            } else if (year <= 0 || year > currentYear || year < 1900) {
                binding.editYear.error = "Please enter a valid year."
                return@setOnClickListener
            } else if (year % 4 == 0 && month == 2 && day > 29) {
                getBar(view)
                return@setOnClickListener
            } else if (year % 4 != 0 && month == 2 && day > 28) {
                getBar(view)
                return@setOnClickListener
            } else {
                val updatedFriend =
                    Friend(friend.id, user?.email.toString(), name, year, month, day, age)
                viewModel.update(updatedFriend)
                findNavController().popBackStack()
            }
        }
    }
}