package com.example.learning_android_mortgage_calculator_kulakov

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import androidx.lifecycle.ViewModelProvider
import com.example.learning_android_mortgage_calculator_kulakov.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), View.OnClickListener, OnItemClickListener {

    private lateinit var binding: ActivityMainBinding

    private val propertyTypes by lazy {
        resources.getStringArray(R.array.property_types)
    }

    private val creditTimes by lazy {
        resources.getStringArray(R.array.credit_times)
    }

    private val viewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvDifferrentiated.setOnClickListener(this)
        binding.tvAnnuity.setOnClickListener(this)
        binding.btnCalculate.setOnClickListener(this)

        val propertyTypesAdapter = NoFilterArrayAdapter(this, propertyTypes)
        binding.etPropertyType.setAdapter(propertyTypesAdapter)

        val creditTimesAdapter = NoFilterArrayAdapter(this, creditTimes)
        binding.etCreditTime.setAdapter(creditTimesAdapter)

        binding.etPropertyType.onItemClickListener = OnItemClickListener { _, _, position, _ ->
            viewModel.setPropertyTypePosition(position)
        }
        binding.etCreditTime.onItemClickListener = OnItemClickListener { _, _, position, _ ->
            viewModel.setCreditTimePosition(position)
        }

        observe()
    }

    private fun observe() {
        viewModel.paymentType.observe(this) {
            binding.tvDifferrentiated.isSelected = it == PaymentType.DIFFERENTIATED
            binding.tvAnnuity.isSelected = it == PaymentType.ANNUITY
        }
        viewModel.calculatedSum.observe(this) {
            binding.tvCalculatedSum.text = it
        }
    }

    override fun onItemClick(p0: AdapterView<*>?, view: View?, position: Int, p3: Long) {
        when (view?.id) {
            R.id.etPropertyType -> viewModel.setPropertyTypePosition(position)
            R.id.etCreditTime -> viewModel.setCreditTimePosition(position)
        }
    }

    override fun onClick(view: View?) {
        when (view) {
            binding.tvDifferrentiated -> {
                viewModel.setPaymentType(PaymentType.DIFFERENTIATED)
            }
            binding.tvAnnuity -> {
                viewModel.setPaymentType(PaymentType.ANNUITY)
            }
            binding.btnCalculate -> {
                val creditSum = binding.etCreditSum.text.toString().toDouble()
                val rate = binding.etRate.text.toString().toDouble()
                viewModel.calculate(creditSum, rate)
            }
        }
    }
}