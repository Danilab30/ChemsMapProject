package mx.itson.cheemstour.interfaces

import mx.itson.cheemstour.entities.Weather
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {

    @GET("weather")
    fun getWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") appid: String,
        @Query("lang") lang: String, //esto servira para que se muestre el idioma dependiendo del sistema
        @Query("units") units: String = "metric" //esto servira para que se muestre los valores en Celsius y no en Kelvin
    ): Call<Weather>
}
