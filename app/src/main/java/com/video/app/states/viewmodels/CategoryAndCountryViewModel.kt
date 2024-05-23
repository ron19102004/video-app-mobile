package com.video.app.states.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.video.app.api.RetrofitAPI
import com.video.app.api.models.CategoryModel
import com.video.app.api.models.CountryModel
import com.video.app.api.repositories.CategoryAndCountryRepository
import com.video.app.config.CONSTANT
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryAndCountryViewModel : ViewModel() {
    private val repository by lazy {
        RetrofitAPI.service(CONSTANT.URL.PATH_URL_DEFAULT).create(CategoryAndCountryRepository::class.java)
    }
    var categories = MutableLiveData<List<CategoryModel>?>(emptyList())
    var countries = MutableLiveData<List<CountryModel>?>(emptyList())

    fun init() {
        fetchCategoriesAndCountries()
    }

    private fun fetchCategoriesAndCountries() {
        viewModelScope.launch {
            try {
                repository.findAllCategory().enqueue(object : Callback<List<CategoryModel>> {
                    override fun onResponse(
                        call: Call<List<CategoryModel>>,
                        response: Response<List<CategoryModel>>
                    ) {
                        if (response.isSuccessful) {
                            val res: List<CategoryModel>? = response.body()
                            categories.value = res
                            Log.e("categories-data", res.toString())
                        }
                    }

                    override fun onFailure(call: Call<List<CategoryModel>>, t: Throwable) {
                        Log.e("categories-data-error", t.toString())
                    }
                })
                repository.findAllCountry().enqueue(object : Callback<List<CountryModel>> {
                    override fun onResponse(
                        call: Call<List<CountryModel>>,
                        response: Response<List<CountryModel>>
                    ) {
                        if (response.isSuccessful) {
                            val res: List<CountryModel>? = response.body()
                            countries.value = res
                            Log.e("countries-data", res.toString())
                        }
                    }

                    override fun onFailure(call: Call<List<CountryModel>>, t: Throwable) {
                        Log.e("countries-data-error", t.toString())
                    }
                })
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("fetch-error", e.toString())
            }
        }
    }
}