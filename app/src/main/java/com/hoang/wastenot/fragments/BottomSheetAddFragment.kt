package com.hoang.wastenot.fragments

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.hoang.wastenot.R
import com.hoang.wastenot.databinding.FragmentBottomSheetAddBinding
import com.hoang.wastenot.models.Food
import com.hoang.wastenot.repositories.CSVReader
import com.hoang.wastenot.repositories.UserRepository
import com.hoang.wastenot.viewmodels.BarcodeSharedViewModel
import com.squareup.picasso.Picasso
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.IOException
import java.util.*


class BottomSheetAddFragment : BottomSheetDialogFragment(), KoinComponent {

    private lateinit var binding: FragmentBottomSheetAddBinding
    private var calendar: Calendar = Calendar.getInstance(Locale.getDefault())
    private lateinit var alarmManager: AlarmManager



    private val viewModel: BarcodeSharedViewModel by activityViewModels()
    private val userRepository: UserRepository by inject()

    private var picUrl: String? = null
    private var expDate: Date? = null
    private var category: String? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bottom_sheet_add, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBottomSheetAddBinding.bind(view)

        //Add product functionality
        readIngredients()
        setOnSaveButtonClicked()
        setOnDatePickerClicked(view)

        binding.btnAddManually.setOnClickListener {
            dismiss()
            findNavController().navigate(R.id.addFragment)
        }
        binding.extraSpace.minimumHeight = Resources.getSystem().displayMetrics.heightPixels

        viewModel.product.observe(viewLifecycleOwner) {
            it?.product?.let { product ->
                binding.layoutAdd.visibility = View.VISIBLE
                binding.btnSaveFood.visibility = View.VISIBLE
                binding.btnAddManually.visibility = View.GONE
                Picasso.get().load(product.imageFrontUrl).into(binding.ivProduct)
                picUrl = product.imageFrontUrl
                if (product.genericName != null && product.genericName != "") {
                    binding.tvProductName.text = product.genericName
                    binding.etFoodName.setText(product.genericName)
                } else {
                    binding.tvProductName.text = product.productName
                    binding.etFoodName.setText(product.productName)
                }
                binding.tvProductBrand.text = product.brands

            }
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

        val categories: MutableList<String> = rows

        ArrayAdapter(requireContext(), R.layout.item_category, categories).also { adapter ->
            textView.setAdapter(adapter)
        }

    }

    private fun setOnDatePickerClicked(view: View) {
        val constraintsBuilder =
            CalendarConstraints.Builder()
                .setValidator(DateValidatorPointForward.now())
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .setCalendarConstraints(constraintsBuilder.build())
                .build()

        binding.btnAddExpDate.setOnClickListener {
            datePicker
                .show(parentFragmentManager, "tag")
            datePicker
                .addOnPositiveButtonClickListener {
                    expDate = Date(it)
                    binding.etDate.setText("${expDate!!.date}-${expDate!!.month + 1}-${expDate!!.year - 100}")
                    setTime(it)
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
                }
                .addOnFailureListener { e ->
                    Log.w("Failure Add Message", "Error adding document", e)
                }
            dismiss()
            Toast.makeText(activity, "Product added!", Toast.LENGTH_SHORT).show()
            setAlarm()
        }
    }

    private fun setTime(time: Long) {
        calendar.timeInMillis = time - 86400000.toLong()
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 35)
        calendar.set(Calendar.SECOND, 30)
    }

    private fun setAlarm() {
        val intent = Intent(context, Notifications::class.java)
        intent
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            notificationId,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )

        Toast.makeText(context, "Alarm set to ${calendar.time}", Toast.LENGTH_LONG).show()
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        viewModel.hideBottomSheetDialog()
    }

    override fun dismiss() {
        super.dismiss()
        viewModel.hideBottomSheetDialog()
    }
}