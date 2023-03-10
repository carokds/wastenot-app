package com.hoang.wastenot.fragments

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context.*
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import coil.load
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.hoang.wastenot.R
import com.hoang.wastenot.databinding.FragmentAddBinding
import com.hoang.wastenot.models.Food
import com.hoang.wastenot.repositories.CSVReader
import com.hoang.wastenot.repositories.UserRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.IOException
import java.util.*
import java.util.Calendar.*

class AddFragment : Fragment(R.layout.fragment_add), KoinComponent {

    private lateinit var alarmManager: AlarmManager
    private var calendar: Calendar = Calendar.getInstance(Locale.getDefault())

    private lateinit var binding: FragmentAddBinding
    private val userRepository: UserRepository by inject()
    private val TAG = "AddFragment"
    private var picUrl: String? = null
    private var expDate: Date? = null
    private var category: String? = null

    private val launchCameraIntentLauncher =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) { fileUri ->
            if (fileUri != null) {
                uploadFromUri(fileUri)
            } else {
                Log.w(TAG, "File URI is null")
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentAddBinding.bind(view)

        setOnHomeBtnClicked(view)
        setOnUploadPictureBtnClicked()

        readIngredients()

        setOnSaveButtonClicked()

        setOnDatePickerClicked(view)

        setStatusBarAppearance()
        
        onClickAddFragmentView(view)
    }

    private fun onClickAddFragmentView(v: View?) {
        val imm = requireActivity().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(v!!.windowToken, 0)
    }

    private fun setStatusBarAppearance() {
        // To show content behind status and navigation bar
        val window = activity?.window
        window?.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
        )
    }


    private fun setOnHomeBtnClicked(view: View) {
        binding.btnHomeAddfragment.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_global_inventoryFragment)
        }
    }

    private fun readIngredients() {
        var rows = mutableListOf<String>()
        val csvReader = CSVReader(requireContext(), "top_1k_ingredients")
        try {
            rows = csvReader.readCSV()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        rows.size

        val textView = (binding.autocompleteCategory) as AutoCompleteTextView
        textView.setError(null)

        val categories: MutableList<String> = rows

        ArrayAdapter(requireContext(), R.layout.item_category, categories).also { adapter ->
            textView.setAdapter(adapter)
            textView.doOnTextChanged { inputText, _, _, _ ->
                if (categories.contains("${textView.text}")) {
                    textView.error = null
                } else {
                    textView.error = "Choose a category from the list"
                }
            }
        }

    }

    private fun setOnDatePickerClicked(view: View) {
        val constraintsBuilder =
            CalendarConstraints.Builder()
                .setValidator(DateValidatorPointForward.now())
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Pick a date")
                .setCalendarConstraints(constraintsBuilder.build())
                .build()

        binding.btnAddExpDate.setOnClickListener {
            datePicker
                .show(parentFragmentManager, "tag")
            datePicker
                .addOnPositiveButtonClickListener {
                    expDate = Date(it)
                    binding.etDate.setText(
                        "${expDate!!.date}-${expDate!!.month + 1}-${expDate!!.year - 100}"
                    )
                    setTime(it)

                }
        }
    }


    private fun setOnUploadPictureBtnClicked() {
        binding.btnAddFoodPicture.setOnClickListener {
            launchCameraIntentLauncher.launch(arrayOf("image/*"))
        }
    }

    private fun uploadFromUri(fileUri: Uri) {
        val storageRef = FirebaseStorage.getInstance().reference
        fileUri.lastPathSegment?.let {
            val photoRef = storageRef.child("pictures").child(it)

            // Upload file to Firebase Storage
            Log.d(TAG, "uploadFromUri:dst:" + photoRef.path)
            photoRef.putFile(fileUri).continueWithTask { task ->
                // Forward any exceptions
                if (!task.isSuccessful) {
                    throw task.exception!!
                }
                Log.d(TAG, "uploadFromUri: upload success")

                // Request the public download URL
                photoRef.downloadUrl

            }.addOnSuccessListener { downloadUri ->
                // Upload succeeded
                Log.d(TAG, "uploadFromUri: getDownloadUri success: $downloadUri")

                picUrl = downloadUri.toString()
                binding.ivSelectedPic.load(picUrl)
                binding.ivAddImage.load(picUrl)

            }.addOnFailureListener { exception ->
                // Upload failed
                Log.w(TAG, "uploadFromUri:onFailure", exception)

            }
        }

    }

    private fun setOnSaveButtonClicked() {
        binding.btnSaveFood.setOnClickListener {

            if (picUrl == null) {
                Toast.makeText(activity, "You haven't selected a picture yet", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            } else if (expDate == null) {
                Toast.makeText(
                    activity,
                    "You haven't selected an expiration date",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val currentUser = userRepository.getCurrentUser() ?: return@setOnClickListener
            val foodName = binding.etFoodName.text.toString()
            category = binding.autocompleteCategory.text.toString()

            val foodRef = Firebase.firestore.collection("foods").document()

            val food = Food(
                foodRef.id,
                foodName,
                Timestamp(expDate!!),
                picUrl,
                category!!,
                currentUser.email
            )

            foodRef.set(food)
                .addOnSuccessListener { documentReference ->
                    Log.d(
                        "Successful Add Message",
                        "DocumentSnapshot added with ID: ${foodRef.id}"
                    )
                    setAlarm()
                }
                .addOnFailureListener { e ->
                    Log.w("Failure Add Message", "Error adding document", e)
                }

            findNavController().navigate(R.id.inventoryFragment)

        }
    }

    private fun setTime(time: Long) {
        calendar.timeInMillis = time - 86400000.toLong()
        calendar.set(HOUR_OF_DAY, 23)
        calendar.set(MINUTE, 35)
        calendar.set(SECOND, 30)
    }

    private fun setAlarm() {
        val intent = Intent(context, Notifications::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            notificationId,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        alarmManager = context?.getSystemService(ALARM_SERVICE) as AlarmManager

        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )
        Toast.makeText(context,"Notification set for ${calendar.time}",Toast.LENGTH_LONG).show()
    }

}


