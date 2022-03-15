package com.hoang.wastenot.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.hoang.wastenot.R
import com.hoang.wastenot.databinding.FragmentInventoryBinding
import com.hoang.wastenot.models.Food
import com.hoang.wastenot.models.User
import com.hoang.wastenot.repositories.UserRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class InventoryFragment : Fragment(R.layout.fragment_inventory), KoinComponent {

    private lateinit var binding: FragmentInventoryBinding
    private val userRepository: UserRepository by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentInventoryBinding.bind(view)

        binding.btnAdd.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_inventoryFragment_to_addFragment)
        }

        binding.ivTemporary.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_inventoryFragment_to_foodDetailFragment)
        }

        firstCheckOfUser()

    }

    private fun firstCheckOfUser() {
        val currentUser = userRepository.getCurrentUser()

        if (currentUser != null) {
            setUserData(currentUser)
        } else {
            launchSignIn()
        }
    }

    private fun setUserData(currentUser: User) {
        binding.tvHello.text = "Hello ${currentUser.displayName}!"

        Firebase.firestore.collection("foods")
            .whereArrayContains("ownerEmail", currentUser.email)
            .addSnapshotListener { documents, e ->
                documents?.map {
                    it.toObject(Food::class.java).apply {
                        id = it.id
                    }
                }
            }
    }

    private fun launchSignIn() {
        activity?.activityResultRegistry?.let {
            userRepository.launchSignIn(it) {
                val currentUser = userRepository.getCurrentUser() ?: return@launchSignIn
                setUserData(currentUser)
            }
        }
    }
}