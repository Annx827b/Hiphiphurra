package com.example.hiphiphurra

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.hiphiphurra.databinding.FragmentAddFriendsBinding
import com.example.hiphiphurra.models.Friend
import com.example.hiphiphurra.repository.FriendsViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import java.util.Calendar

class AddFriendsFragment : Fragment() {
    private var _binding: FragmentAddFriendsBinding? = null
    private var base: FirebaseAuth = FirebaseAuth.getInstance()
    private val viewModel: FriendsViewModel by activityViewModels()
    private val binding get() = _binding!!

    private var day: Int = 1
    private var month: Int = 1

    fun getSpinner(spinner: Spinner) {
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                Log.d("APPLE", "Changing spinner")
                if (spinner == binding.daySpinner) {
                    day = pos + 1
                }
                if (spinner == binding.monthSpinner) {
                    month = pos + 1
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                Log.d("Apple", "Nothing was called")
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddFriendsBinding.inflate(inflater, container, false)
        return binding.root
    }

    fun getBar(view: View) {
        Snackbar.make(view, "Date not valid", Snackbar.LENGTH_LONG)
            .setAction("Action", null).show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val spinnerMonth: Spinner = binding.monthSpinner
        getSpinner(spinnerMonth)
        val spinnerDay: Spinner = binding.daySpinner
        getSpinner(spinnerDay)
        val user = base.currentUser

        binding.add.setOnClickListener {
            val year = binding.editTextYear.text.trim().toString().toIntOrNull() ?: 0
            val currentYear = Calendar.getInstance().get(Calendar.YEAR)
            val name = binding.editTextName.text.trim().toString()

            if (name.isEmpty()) {
                binding.editTextName.error = "Please enter a name."
                return@setOnClickListener
            } else if (year <= 0 || year > currentYear || year < 1900) {
                binding.editTextYear.error = "Please enter a valid year."
                return@setOnClickListener
            }
            else if(year % 4 == 0 && month == 2 && day > 29){
                getBar(view)
                return@setOnClickListener
            }else if(year % 4 != 0 &&month == 2 && day > 28) {
                getBar(view)
                return@setOnClickListener
            } else {
                val friendNew = Friend(user?.email.toString(), name, year, month, day)
                viewModel.add(friendNew)
                findNavController().popBackStack()
            }
        }
        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}