package mx.itson.cheemstour.interfaces

import mx.itson.cheemstour.entities.Trip
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface CheemsAPI {

    //Leer los viajes
    @GET("trips")
    fun getTrips(): Call<List<Trip>>

    //Crear un viaje
    @POST("trip")
    fun saveTrip(@Body trip: Trip): Call<Boolean>

    //Eliminar un viaje
    @DELETE("trip/{id}")
    fun deleteTrip(@Path("id") id: Int): Call<Void>

    //Actualizar un viaje
    @PUT("trip/{id}")
    fun updateTrip(@Path("id") id: Int, @Body trip: Trip): Call<Void>
}