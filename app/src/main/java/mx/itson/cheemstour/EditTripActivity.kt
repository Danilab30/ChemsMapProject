package mx.itson.cheemstour

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import mx.itson.cheemstour.entities.Trip
import mx.itson.cheemstour.utils.RetrofitUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditTripActivity : AppCompatActivity() {

    private var tripId: Int = 0
    private lateinit var txtName: EditText
    private lateinit var txtCity: EditText
    private lateinit var txtCountry: EditText
    private lateinit var txtLatitude: EditText
    private lateinit var txtLongitude: EditText
    private lateinit var btnSave: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_trip)

        txtName = findViewById(R.id.txtName)
        txtCity = findViewById(R.id.txtCity)
        txtCountry = findViewById(R.id.txtCountry)
        txtLatitude = findViewById(R.id.txtLatitude)
        txtLongitude = findViewById(R.id.txtLongitude)
        btnSave = findViewById(R.id.btnSave)

        // Recibe los datos del viaje desde el intent
        tripId = intent.getIntExtra("trip_id", 0)
        txtName.setText(intent.getStringExtra("trip_name"))
        txtCity.setText(intent.getStringExtra("trip_city"))
        txtCountry.setText(intent.getStringExtra("trip_country"))
        txtLatitude.setText(intent.getDoubleExtra("trip_latitude", 0.0).toString())
        txtLongitude.setText(intent.getDoubleExtra("trip_longitude", 0.0).toString())

        btnSave.setOnClickListener {
            val updatedTrip = Trip(
                id = tripId,
                name = txtName.text.toString(),
                city = txtCity.text.toString(),
                country = txtCountry.text.toString(),
                latitude = txtLatitude.text.toString().toDouble(),
                longitude = txtLongitude.text.toString().toDouble()
            )

            RetrofitUtil.getApi()?.updateTrip(tripId, updatedTrip)
                ?.enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        Toast.makeText(this@EditTripActivity, "Actualizado correctamente", Toast.LENGTH_SHORT).show()
                        finish()
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Toast.makeText(this@EditTripActivity, "Error al actualizar", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }
}
