package mx.itson.cheemstour.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import mx.itson.cheemstour.R
import mx.itson.cheemstour.entities.Trip

class TripAdapter(
    context : Context,
    trips : List<Trip>
) : BaseAdapter() {

    var context : Context = context
    var tripsList : List<Trip> = trips

    override fun getCount(): Int {
        return tripsList.size
    }

    override fun getItem(position: Int): Any {
        return tripsList[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var elemento = LayoutInflater.from(context).inflate(R.layout.elem_trip, null)
        try {
            val trip = getItem(position) as Trip

            val txtName: TextView = elemento.findViewById(R.id.trip_name)
            txtName.text = trip.name

            val txtCity: TextView = elemento.findViewById(R.id.trip_city)
            txtCity.text = trip.city

            val txtCountry: TextView = elemento.findViewById(R.id.trip_country)
            txtCountry.text = trip.country

        } catch(ex: Exception) {
            Log.e("Error showing trips", ex.message.toString())
        }
        return elemento
    }
}