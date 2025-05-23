package mx.itson.cheemstour

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import android.widget.Toast
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

    fun eliminarViaje(id: Int) {
        val call = RetrofitUtil.getApi()!!.deleteTrip(id)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                Toast.makeText(this@TripListActivity, getString(R.string.text_delete_successful), Toast.LENGTH_SHORT)
                    .show()
                getTrips() // Recargar lista
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@TripListActivity, getString(R.string.text_delete_error), Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }


    fun getTrips() {
        val call: Call<List<Trip>> = RetrofitUtil.getApi()!!.getTrips()
        call.enqueue(object : Callback<List<Trip>> {
            override fun onResponse(call: Call<List<Trip>>, response: Response<List<Trip>>) {
                if (response.isSuccessful && response.body() != null) {
                    val trips: List<Trip> = response.body()!!
                    listTrips?.adapter = TripAdapter(
                        context,
                        trips,
                        onUpdateClick = { trip ->
                            val intent = Intent(this@TripListActivity, EditTripActivity::class.java)
                            intent.putExtra("trip_id", trip.id)
                            intent.putExtra("trip_name", trip.name)
                            intent.putExtra("trip_city", trip.city)
                            intent.putExtra("trip_country", trip.country)
                            intent.putExtra("trip_latitude", trip.latitude)
                            intent.putExtra("trip_longitude", trip.longitude)
                            startActivity(intent)
                        },
                        onDeleteClick = { trip ->
                            AlertDialog.Builder(this@TripListActivity)
                                .setTitle(getString(R.string.text_confirm_delete))
                                .setMessage( getString(R.string.text_delete_question) + "\"${trip.name}\"?")
                                .setPositiveButton(getString(R.string.text_yes)) { _, _ ->
                                    trip.id?.let { eliminarViaje(it) }
                                }
                                .setNegativeButton(getString(R.string.text_no), null)
                                .show()
                        }
                    )
                } else {
                    Toast.makeText(
                        this@TripListActivity,
                        getString(R.string.text_server_error),
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e("API", "Error: ${response.code()} ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<List<Trip>>, t: Throwable) {
                Log.e("API", getString(R.string.text_api_failed) + " ${t.message}")
                Toast.makeText(
                    this@TripListActivity,
                    getString(R.string.text_server_connection_failed),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

}