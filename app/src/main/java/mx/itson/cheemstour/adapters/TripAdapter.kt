package mx.itson.cheemstour.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import mx.itson.cheemstour.R
import mx.itson.cheemstour.entities.Trip

class TripAdapter(
    val context: Context,
    val tripsList: List<Trip>,
    val onUpdateClick: (Trip) -> Unit,
    val onDeleteClick: (Trip) -> Unit
) : BaseAdapter() {

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
        val elemento = LayoutInflater.from(context).inflate(R.layout.elem_trip, null)
        val trip = tripsList[position]

        elemento.findViewById<TextView>(R.id.trip_name).text = trip.name
        elemento.findViewById<TextView>(R.id.trip_city).text = trip.city
        elemento.findViewById<TextView>(R.id.trip_country).text = trip.country

        val btnUpdate = elemento.findViewById<Button>(R.id.btn_update)
        val btnDelete = elemento.findViewById<Button>(R.id.btn_delete)

        btnUpdate.setOnClickListener { onUpdateClick(trip) }
        btnDelete.setOnClickListener { onDeleteClick(trip) }

        return elemento
    }

}