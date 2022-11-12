/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.cupcake.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/** harga 1 bunga */
private const val PRICE_PER_CUPCAKE = 5000.00

/** harga ketika diambil hari ini akan ditambah biaya */
private const val PRICE_FOR_SAME_DAY_PICKUP = 6000.00


// orderviewmodel digunakan sebagai viewmodel
// orderviewmodel menyimpan informasi tentang order bunga yaitu jumlah, jenis dan tanggal pengambilan
class OrderViewModel : ViewModel() {
// livedata
    // jumlah bunga yang diorder
    private val _quantity = MutableLiveData<Int>()
    val quantity: LiveData<Int> = _quantity

    // jenis bunga yang diorder
    private val _flavor = MutableLiveData<String>()
    val flavor: LiveData<String> = _flavor

    // pilihan tanggal
    val dateOptions: List<String> = getPickupOptions()

    // tanggal pengambilan
    private val _date = MutableLiveData<String>()
    val date: LiveData<String> = _date

    // Price of the order so far
    private val _price = MutableLiveData<Double>()
    val price: LiveData<String> = Transformations.map(_price) {
        // Format the price into the local currency and return this as LiveData<String>
        NumberFormat.getCurrencyInstance().format(it)
    }

    init {
        // Set initial values for the order
        resetOrder()
    }

    /**
     *  untuk mengatur jumlah bunga yang dipesan
     *
     * @param numberCupcakes to order
     */
    fun setQuantity(numberCupcakes: Int) {
        _quantity.value = numberCupcakes
        updatePrice()
    }

    /**
     * digunakan untuk mengatur jenis buanga yang diesan
     *
     * @param desiredFlavor  dengan tipe data string
     */
    fun setFlavor(desiredFlavor: String) {
        _flavor.value = desiredFlavor
    }

    /**
     * mengatur tanggal pengambilan
     *
     * @param pickupDate dengan tipe data  string
     */
    fun setDate(pickupDate: String) {
        _date.value = pickupDate
        updatePrice()
    }

    /**
     * mengambalikan nilai jenis bunga jika bernilai benar
     */
    fun hasNoFlavorSet(): Boolean {
        return _flavor.value.isNullOrEmpty()
    }

    /**
     * mengatur ulang pesanan dengan menggunakan nilai default awal untuk jumlah, jenis, tanggal, dan harga.
     */
    fun resetOrder() {
        _quantity.value = 0
        _flavor.value = ""
        _date.value = dateOptions[0]
        // digunakan untuk mengatur jumalah mata uang / jenis mata uang
        _price.value = 000.00
    }

    /**
     * menampilkan ulang atau update harga dan detil pesanan.
     */
    private fun updatePrice() {
        var calculatedPrice = (quantity.value ?: 0) * PRICE_PER_CUPCAKE
        // jika diambil hari ini maka dikenakan biaya tambahan
        if (dateOptions[0] == _date.value) {
            calculatedPrice += PRICE_FOR_SAME_DAY_PICKUP
        }
        _price.value = calculatedPrice
    }

    /**
     * menampilkan daftar tanggal pengambilan
     */
    private fun getPickupOptions(): List<String> {
        val options = mutableListOf<String>()
        //digunakan untuk mengatur tangal dan akan tampil sesui dengan waktu di lokal
        val formatter = SimpleDateFormat("E MMM d", Locale.getDefault())
        val calendar = Calendar.getInstance()
        // digunakan untuk menetukan jumlah tanggal yang kan di tampilkan
        repeat(7) {
            options.add(formatter.format(calendar.time))
            calendar.add(Calendar.DATE, 1)
        }
        return options
    }
}