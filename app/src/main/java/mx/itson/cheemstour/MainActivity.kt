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
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnTripList = findViewById<View>(R.id.button_trip_list) as Button
        btnTripList.setOnClickListener(this)

        val btnTripForm = findViewById<View>(R.id.button_trip_form) as Button
        btnTripForm.setOnClickListener(this)

        val btnTripMap = findViewById<View>(R.id.button_trip_map) as Button
        btnTripMap.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.button_trip_list -> {
                Toast.makeText(this, getString(R.string.text_open_list), Toast.LENGTH_SHORT).show()
                vibrate(100)
                val intentListTrip = Intent(this, TripListActivity::class.java)
                startActivity(intentListTrip)
            }

            R.id.button_trip_form -> {
                Toast.makeText(this, getString(R.string.text_open_form), Toast.LENGTH_SHORT).show()
                vibrate(100)
                val intentListForm = Intent(this, TripFormActivity::class.java)
                startActivity(intentListForm)
            }

            R.id.button_trip_map -> {
                Toast.makeText(this, getString(R.string.text_open_map), Toast.LENGTH_SHORT).show()
                vibrate(100)
                val intentListForm = Intent(this, TripMapActivity::class.java)
                startActivity(intentListForm)
            }
        }
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