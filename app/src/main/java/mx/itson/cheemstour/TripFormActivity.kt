package mx.itson.cheemstour

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import mx.itson.cheemstour.entities.Trip
import mx.itson.cheemstour.utils.RetrofitUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TripFormActivity : AppCompatActivity(), View.OnClickListener, OnMapReadyCallback {

    var map: GoogleMap? = null
    lateinit var name: EditText
    lateinit var city: EditText
    lateinit var country: EditText
    var latitude: Double = 0.0
    var longitude: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_trip_form)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnSave = findViewById<View>(R.id.button_save) as Button
        btnSave.setOnClickListener(this)

        name = findViewById(R.id.txt_name)
        city = findViewById(R.id.txt_city)
        country = findViewById(R.id.txt_country)

        var mapaFragment =
            supportFragmentManager.findFragmentById(R.id.map_form) as SupportMapFragment
        mapaFragment.getMapAsync(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.button_save -> {
                saveTrip(
                    Trip(
                        0,
                        name.text.toString(),
                        city.text.toString(),
                        country.text.toString(),
                        latitude,
                        longitude
                    )
                )
            }
        }
    }

    fun saveTrip(trip: Trip) {
        val context = this
        val call: Call<Boolean> = RetrofitUtil.getApi()!!.saveTrip(trip)
        call.enqueue(object : Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                if (response.isSuccessful) {
                    val isSuccess: Boolean = response.body() ?: false
                    if (isSuccess) {
                        val intentMain = Intent(context, MainActivity::class.java)
                        startActivity(intentMain)

                        Toast.makeText(context,getString(R.string.text_saved_successful),Toast.LENGTH_LONG).show()
                        vibrate(500)
                    } else {
                        Toast.makeText(context,getString(R.string.text_saved_error),Toast.LENGTH_LONG).show()
                    }
                } else {
                    Log.e("Error", "Failed to save trip: ${response.code()}")
                }
            }
            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                Log.e("Error", "Error calling API: ${t.message}")
            }
        })
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map!!.mapType = GoogleMap.MAP_TYPE_HYBRID

        var latLng = LatLng(0.0, 0.0)
        map?.addMarker(MarkerOptions().position(latLng).draggable(true))

        map?.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        map?.animateCamera(CameraUpdateFactory.zoomTo(8f))

        map?.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener {
            override fun onMarkerDrag(p0: Marker) {

            }

            override fun onMarkerDragEnd(marker: Marker) {
                latitude = marker.position.latitude
                longitude = marker.position.longitude
                vibrate(150)
                Toast.makeText(this@TripFormActivity, getString(R.string.text_marker_set), Toast.LENGTH_SHORT).show()
            }

            override fun onMarkerDragStart(p0: Marker) {

            }

        })

    }

    private fun vibrate(duration: Long) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            val vibrator = vibratorManager.defaultVibrator
            vibrator.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            vibrator.vibrate(duration)
        }
    }

}