package com.hoang.wastenot.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.hoang.wastenot.R
import com.hoang.wastenot.adapters.FoodInventoryAdapter
import com.hoang.wastenot.databinding.FragmentInventoryBinding
import com.hoang.wastenot.models.Food
import com.hoang.wastenot.models.User
import com.hoang.wastenot.repositories.UserRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class InventoryFragment : Fragment(R.layout.fragment_inventory), KoinComponent {

    private lateinit var binding: FragmentInventoryBinding

    private val userRepository: UserRepository by inject()

    private val foodsInventoryAdapter: FoodInventoryAdapter
        get() = binding.rvFoodsInventory.adapter as FoodInventoryAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentInventoryBinding.bind(view)

        binding.rvFoodsInventory.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            adapter = FoodInventoryAdapter().apply {
                onItemClicked = {
                    val bundle = Bundle()
                    bundle.putParcelable("Food", it)
                    findNavController().navigate(
                        R.id.action_inventoryFragment_to_foodDetailFragment,
                        bundle
                    )
                }

                onDeleteBtnClicked = { food ->
                    Firebase.firestore.collection("foods").document(food.id)
                        .delete()
                        .addOnSuccessListener { Log.d("x", "DocumentSnapshot successfully deleted!") }
                        .addOnFailureListener { e -> Log.w("x", "Error deleting document", e) }
                }

            }
        }

        firstCheckOfUser()

        setOnAddBtnClicked(view)

        setOnLogoutBtnClicked()

    }

    private fun setOnAddBtnClicked(view: View) {
        binding.btnAdd.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_inventoryFragment_to_addFragment)
        }
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
            .whereEqualTo("ownerEmail", currentUser.email)
            .addSnapshotListener { documents, e ->
                documents?.map {
                    it.toObject(Food::class.java).apply {
                        id = it.id
                    }
                }?.let { foodsInventoryAdapter.setData(it) }
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

    private fun setOnLogoutBtnClicked() {
        binding.btnLogout.setOnClickListener {
            userRepository.signOut(requireContext()) {
                launchSignIn()
            }
        }
    }
}