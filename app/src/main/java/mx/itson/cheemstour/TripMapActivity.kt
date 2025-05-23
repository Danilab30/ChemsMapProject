package mx.itson.cheemstour

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import mx.itson.cheemstour.entities.Trip
import mx.itson.cheemstour.entities.Weather
import mx.itson.cheemstour.utils.RetrofitUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.appcompat.app.AlertDialog

class TripMapActivity : AppCompatActivity(), OnMapReadyCallback {

    var map: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_trip_map)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val mapaFragment = supportFragmentManager.findFragmentById(R.id.map_form) as SupportMapFragment
        mapaFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        try {
            map = googleMap
            map?.mapType = GoogleMap.MAP_TYPE_NORMAL


            getTrips()

            // Al hacer clic en un marcador, obtenemos el clima de esa ubicación
            map?.setOnMarkerClickListener { marker ->
                val position = marker.position
                obtenerClimaDesdeLatLng(position.latitude, position.longitude, marker.title)
                true
            }

        } catch (e: Exception) {
            Log.e("Error loading map", e.message.toString())
        }
    }

    fun getTrips() {
        val call: Call<List<Trip>> = RetrofitUtil.getApi()!!.getTrips()
        call.enqueue(object : Callback<List<Trip>> {
            override fun onResponse(call: Call<List<Trip>>, response: Response<List<Trip>>) {
                val trips: List<Trip> = response.body()!!
                trips.forEach { t ->
                    if (t.latitude != 0.0) {
                        val latLng = LatLng(t.latitude, t.longitude)
                        map?.addMarker(
                            MarkerOptions()
                                .position(latLng)
                                .title(t.name)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.cheems))
                        )
                    }
                }
            }

            override fun onFailure(call: Call<List<Trip>>, t: Throwable) {
                Log.e("Error", "Error calling API: ${t.message}")
            }
        })
    }

    fun obtenerClimaDesdeLatLng(lat: Double, lon: Double, nombreLugar: String?) { //parametros de entrada latitud y longitud junto con el nombre del lugar

        val idioma = java.util.Locale.getDefault().language
        val call: Call<Weather> = RetrofitUtil.getApiWeather()!!
            .getWeather(lat, lon, "9ebf4ea47131a883ab8a265189f58ef7", idioma)
            //llamada a la API key y la latitud y longitud

        call.enqueue(object : Callback<Weather> { //ejecuta la petición
            override fun onResponse(call: Call<Weather>, response: Response<Weather>) {
                val weather: Weather? = response.body()
                //se obtiene el objeto Weather de la respuesta que la descripcion del clima

                if (weather != null) {
                    vibrate(300)
                    val descripcion = weather.weather?.firstOrNull()?.description ?: "Sin descripción"
                    //es una lista de objetos que viene en el json, si el primer elemento no es nulo se obtiene la descripcion



                    val temperatura = weather.main?.temp ?: 0.0
                    val sensacion = weather.main?.feels_like ?: 0.0
                    val tempMin = weather.main?.temp_min ?: 0.0
                    val tempMax = weather.main?.temp_max ?: 0.0
                    val presion = weather.main?.pressure ?: 0
                    val humedad = weather.main?.humidity ?: 0




                    val mensaje = """
                        ${getString(R.string.text_place)}: $nombreLugar
                        ${getString(R.string.text_description)}: ${weather.weather?.firstOrNull()?.description ?: "-"}
                        ${getString(R.string.text_temperature)}: ${"%.1f".format(temperatura)}°C
                        ${getString(R.string.text_feels_like)}: ${"%.1f".format(sensacion)}°C
                        ${getString(R.string.text_temp_min)}: ${"%.1f".format(tempMin)}°C
                        ${getString(R.string.text_temp_max)}: ${"%.1f".format(tempMax)}°C
                        ${getString(R.string.text_pressure)}: $presion hPa
                        ${getString(R.string.text_humidity)}: $humedad%
                        """.trimIndent()

                    AlertDialog.Builder(this@TripMapActivity)
                        .setTitle(getString(R.string.text_weather_title))
                        .setMessage(mensaje)
                        .setPositiveButton(getString(R.string.text_accept), null)
                        .show()

                    //trimIndent() hace que el mensaje del clima se muestre sin espacio
                    //es un texto para el usuario vea el mensaje de clima
                    //el %.1f es para que muestre solo 1 decimal

                } else {
                    Toast.makeText(this@TripMapActivity, getText(R.string.text_weather_error), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Weather>, t: Throwable) {
                Toast.makeText(this@TripMapActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
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
