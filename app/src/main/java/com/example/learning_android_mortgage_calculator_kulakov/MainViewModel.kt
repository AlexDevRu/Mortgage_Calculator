package com.example.learning_android_mortgage_calculator_kulakov

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlin.math.pow

class MainViewModel(private val app: Application): AndroidViewModel(app) {

    private val creditTimesValues = arrayOf(5, 10, 15)

    private val _propertyType = MutableLiveData(PropertyType.ANY)
    val propertyType : LiveData<PropertyType> = _propertyType

    private val _creditTime = MutableLiveData(creditTimesValues[0])
    val creditTime : LiveData<Int> = _creditTime

    private val _calculatedSum = MutableLiveData<String>()
    val calculatedSum : LiveData<String> = _calculatedSum

    private val _paymentType = MutableLiveData(PaymentType.DIFFERENTIATED)
    val paymentType : LiveData<PaymentType> = _paymentType

    fun setPropertyTypePosition(position: Int) {
        _propertyType.value = PropertyType.values()[position]
    }

    fun setCreditTimePosition(position: Int) {
        _creditTime.value = creditTimesValues[position]
    }

    fun setPaymentType(paymentType: PaymentType) {
        _paymentType.value = paymentType
    }

    fun calculate(creditSum: Double, rate: Double) {
        val report = StringBuilder()

        if (paymentType.value == PaymentType.DIFFERENTIATED) {
            val months = creditTime.value!! * 12
            val creditBody = creditSum / months

            var remainSum = creditSum
            val rateInPercents = rate / 100

            report.append(app.getString(R.string.monthly_payment_line, creditBody))

            for(monthIndex in 1..months) {
                val sum = remainSum * rateInPercents * 31 / 365

                val line = app.getString(R.string.percent_payment_line, monthIndex, sum)
                report.append(line)

                remainSum -= creditBody
            }
        } else {
            val i = rate / 100 / 12
            val n = creditTime.value!! * 12.0
            val k = (i * (1 + i).pow(n))/((1 + i).pow(n) - 1)
            val a = k * creditSum
            report.append(app.getString(R.string.monthly_payment_line, a))

            val overpay = a * n - creditSum
            report.append(app.getString(R.string.overpayment_line, overpay))
        }

        _calculatedSum.value = report.toString()
    }

}