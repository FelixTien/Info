package com.felix.fetchtest.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import com.felix.fetchtest.R
import com.felix.fetchtest.databinding.ActivityMainBinding
import com.felix.fetchtest.databinding.DetailBinding
import com.felix.fetchtest.model.Fetch
import com.felix.fetchtest.model.FetchApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    var fetchData: List<Fetch> = mutableListOf()
    var fetchTest: MutableList<Fetch> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var infoLayout: LinearLayout = findViewById(R.id.information)

        // Parse Fetch Test data
        val retrofitFetch = Retrofit.Builder()
            .baseUrl("https://fetch-hiring.s3.amazonaws.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val fetchApi = retrofitFetch.create(FetchApi::class.java)

        fetchApi.getFetch().enqueue(object : Callback<List<Fetch>> {
            override fun onResponse(call: Call<List<Fetch>>, response: Response<List<Fetch>>) {
                if(response.isSuccessful){
                    fetchData = response.body() as List<Fetch>
                    // Sort the data
                    for(i in fetchData.indices){
                        if(fetchData[i].name == "" || fetchData[i].name == null) continue
                        var check = 0
                        for(j in 0 until fetchTest.size){
                            if(fetchData[i].listId < fetchTest[j].listId){
                                fetchTest.add(j, fetchData[i])
                                check++
                                break
                            }
                            else if(fetchData[i].listId == fetchTest[j].listId){
                                if(fetchData[i].name!! < fetchTest[j].name!!){
                                    fetchTest.add(j, fetchData[i])
                                    check++
                                    break
                                }
                            }
                        }
                        if(check == 0) fetchTest.add(fetchData[i])
                    }
                    binding.title.text = "Fetch Test Result"
                    for(i in 0 until fetchTest.size)
                        setData(infoLayout, fetchTest[i].id, fetchTest[i].listId, fetchTest[i].name!!)
                }
            }
            override fun onFailure(call: Call<List<Fetch>>, t: Throwable) {
                // Handle error
            }
        })
    }
    fun setData(infoLayout: LinearLayout, id: Int, listId: Int, name: String){
        val scale: Float = resources.displayMetrics.density
        val detailBinding = DetailBinding.inflate(layoutInflater)
        val detailView = detailBinding.root
        detailBinding.id.text = id.toString()
        detailBinding.listId.text = listId.toString()
        detailBinding.name.text = name

        val detailParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        detailParams.height = 520
        detailParams.setMargins((5 * scale + 0.5f).toInt(), 0, (5 * scale + 0.5f).toInt(), 0)
        detailView.layoutParams = detailParams
        infoLayout.addView(detailView)
    }
}