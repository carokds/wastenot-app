package com.hoang.wastenot.fragments

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.datepicker.MaterialDatePicker
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
import java.time.Instant.now
import java.util.*

class InventoryFragment : Fragment(R.layout.fragment_inventory), KoinComponent {

    private lateinit var binding: FragmentInventoryBinding

    private val userRepository: UserRepository by inject()

    private val foodsInventoryAdapter: FoodInventoryAdapter
        get() = binding.rvFoodsInventory.adapter as FoodInventoryAdapter

    private val rotateOpen: Animation by lazy {
        AnimationUtils.loadAnimation(
            activity,
            R.anim.rotate_open_anim
        )
    }
    private val rotateClose: Animation by lazy {
        AnimationUtils.loadAnimation(
            activity,
            R.anim.rotate_close_anim
        )
    }
    private val fromBottom: Animation by lazy {
        AnimationUtils.loadAnimation(
            activity,
            R.anim.from_bottom_anim
        )
    }
    private val toBottom: Animation by lazy {
        AnimationUtils.loadAnimation(
            activity,
            R.anim.to_bottom_anim
        )
    }
    private var clicked: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentInventoryBinding.bind(view)

        setInitialisation()

        firstCheckOfUser()

        setOnAddBtnClicked(view)

        setOnLogoutBtnClicked()

    }

    private fun setInitialisation() {
        binding.rvFoodsInventory.apply {

            layoutManager = GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false)
            adapter = FoodInventoryAdapter().apply {

                onItemClicked = {
                    val bundle = Bundle()
                    bundle.putParcelable("Food", it)
                    findNavController().navigate(
                        R.id.action_inventoryFragment_to_foodDetailFragment,
                        bundle
                    )
                }

            }
        }
    }



    private fun setOnAddBtnClicked(view: View) {
        binding.btnAdd.setOnClickListener {
            setVisibility(clicked)
            setAnimation(clicked)
            setClickable(clicked)
            clicked = !clicked
        }

        binding.btnAddByScan.setOnClickListener {

        }

        binding.btnAddManually.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_inventoryFragment_to_addFragment)
        }
    }

    private fun setClickable(clicked: Boolean) {
        binding.btnAddByScan.isClickable = !clicked
        binding.btnAddManually.isClickable = !clicked
    }

    private fun setAnimation(clicked: Boolean) {
        if (!clicked) {
            binding.btnAdd.startAnimation(rotateOpen)
            binding.btnAddManually.startAnimation(fromBottom)
            binding.btnAddByScan.startAnimation(fromBottom)
        } else {
            binding.btnAdd.startAnimation(rotateClose)
            binding.btnAddManually.startAnimation(toBottom)
            binding.btnAddByScan.startAnimation(toBottom)
        }
    }

    private fun setVisibility(clicked: Boolean) {
        if (!clicked) {
            binding.btnAddByScan.visibility = View.VISIBLE
            binding.btnAddManually.visibility = View.VISIBLE
        } else {
            binding.btnAddByScan.visibility = View.INVISIBLE
            binding.btnAddManually.visibility = View.INVISIBLE
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