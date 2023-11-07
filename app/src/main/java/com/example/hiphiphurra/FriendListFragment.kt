package com.example.hiphiphurra

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hiphiphurra.databinding.FriendsListFragmentBinding
import com.example.hiphiphurra.repository.FriendsViewModel
import com.google.firebase.auth.FirebaseAuth

class AllFriendsFragment : Fragment() {
    private var _binding: FriendsListFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FriendsViewModel by activityViewModels()
    private var base: FirebaseAuth = FirebaseAuth.getInstance()
    private var idUser: String? = base.currentUser?.email

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FriendsListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.friendLiveData.observe(viewLifecycleOwner) {friends ->
            binding.progressbar.visibility = View.GONE
            binding.recyclerView.visibility = if (friends == null) View.GONE else View.VISIBLE
            binding.textView.text = base.currentUser?.email
            if (friends != null) {
                val adapter = MyAdapter(friends) {pos ->
                    val action =
                        FriendListFragmentDirections.actionFriendsListFragmentToFriendFragmentriendFragment(pos)
                    findNavController().navigate(action)
                }

                binding.recyclerView.layoutManager = LinearLayoutManager(activity)
                binding.recyclerView.adapter = adapter

                val spinner: Spinner = binding.sorting
                spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long
                    ) {
                        Log.d("APPLE", "Changing Spinner")

                        if(pos == 1){viewModel.sortByName()}
                        if(pos == 2){viewModel.sortByNameDescending()}
                        if(pos == 3){viewModel.sortByAge()}
                        if(pos == 4){viewModel.sortByAgeDescending()}
                        if(pos == 5){viewModel.sortByBirth()}
                        if(pos == 6){viewModel.sortByBirthDescending()}
                    }
                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        Log.d("APPLE", "Nothing was called?")
                    }
                }
                binding.filter.setOnClickListener {
                    val name = binding.editFilterName.text.toString().trim()
                    viewModel.filter(name, idUser)
                }
            }
            binding.addButton.setOnClickListener {
                findNavController().navigate(R.id.action_friendsListFragment_to_addFragment)
            }
        }
        viewModel.reload(idUser)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}