package mx.itson.cheemstour

import android.os.Bundle
import android.util.Log
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import mx.itson.cheemstour.adapters.TripAdapter
import mx.itson.cheemstour.entities.Trip
import mx.itson.cheemstour.utils.RetrofitUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TripListActivity : AppCompatActivity() {

    var listTrips: ListView? = null

    val context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_trip_list)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        listTrips = findViewById(R.id.list_trips)

        //val animales : List<Trip> = Trip().get(this)
        //listTrips?.adapter = TripAdapter(this, animales)
        getTrips()
    }

    fun getTrips() {
        val call: Call<List<Trip>> = RetrofitUtil.getApi()!!.getTrips()
        call.enqueue(object: Callback<List<Trip>> {
            override fun onResponse(call: Call<List<Trip>>, response: Response<List<Trip>>) {
                val trips : List<Trip> = response.body()!!
                listTrips?.adapter = TripAdapter(context, trips)
            }
            override fun onFailure(call: Call<List<Trip>>, t: Throwable) {
                Log.e("Error", "Error calling API")
            }
        })
    }
}