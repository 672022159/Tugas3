package com.example.helloworldspikotlin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.helloworldspikotlin.databinding.ActivityMainBinding
import org.json.JSONObject
import java.net.URL
import kotlin.concurrent.thread
import java.text.NumberFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MyViewModel
    private val allUsers = mutableListOf<User>()
    private var currentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(MyViewModel::class.java)

        //  pencarian data user
        viewModel.users.observe(this) { users ->
            allUsers.clear()
            allUsers.addAll(users)
            currentIndex = 0
            showUser(currentIndex)
        }

        viewModel.errorMessage.observe(this) { errorMessage ->
            binding.txtFirstName.text = errorMessage
            binding.txtLastName.text = ""
            binding.txtUniversity.text = ""
        }

        // Tombol Get Users untuk mengambila data yang sudah ditemukan 
        binding.btnGetUsers.setOnClickListener {
            viewModel.fetchUsers()
        }

        // Tombol Previous untuk melihat sebelum nya  
        binding.btnPrevious.setOnClickListener {
            if (currentIndex > 0) {
                currentIndex--
                showUser(currentIndex)
            }
        }

        // Tombol Next untuk melihat data berikut nya
        binding.btnNext.setOnClickListener {
            if (currentIndex < allUsers.size - 1) {
                currentIndex++
                showUser(currentIndex)
            }
        }

        // Tombol Get Products (Harga Rata-Rata)
        binding.btnGetProducts.setOnClickListener {
            fetchAverageProductPrice()
        }
    }

    private fun showUser(index: Int) {
        val user = allUsers[index]
        binding.txtFirstName.text = user.firstName
        binding.txtLastName.text = user.lastName
        binding.txtUniversity.text = user.university

        // tombol previous di data yang pertama dia tidak bisa di tekan  
        binding.btnPrevious.isEnabled = index > 0
        binding.btnNext.isEnabled = index < allUsers.size - 1
    }

    private fun fetchAverageProductPrice() {
        thread {
            try {
                val response = URL("https://dummyjson.com/products").readText()
                val json = JSONObject(response)
                val products = json.getJSONArray("products")
                var totalPrice = 0.0

                for (i in 0 until products.length()) {
                    val product = products.getJSONObject(i)
                    totalPrice += product.getDouble("price")
                }

                val average = totalPrice / products.length()

                val formattedPrice = NumberFormat.getCurrencyInstance(Locale("in", "ID")).format(average)

                runOnUiThread {
                    binding.tvAveragePrice.text = "Rata-rata harga produk: $formattedPrice"
                }
            //jika data product tidak ditemukan maka akan muncul proteksi yang menyatakan data product gagal di ambil 
            } catch (e: Exception) {
                runOnUiThread {
                    binding.tvAveragePrice.text = "Gagal mengambil data produk"
                }
            }
        }
    }
}
