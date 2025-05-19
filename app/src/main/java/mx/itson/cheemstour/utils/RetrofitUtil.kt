package mx.itson.cheemstour.utils

import com.google.gson.GsonBuilder
import mx.itson.cheemstour.entities.Weather
import mx.itson.cheemstour.interfaces.CheemsAPI
import mx.itson.cheemstour.interfaces.WeatherAPI
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitUtil {

    private fun createHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY

            //Se usa normalmente para configurar la red en apps Android o Kotlin con control detallado de tiempos y logs.
        }

        return OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.MINUTES) // Tiempo máximo para conectar
            .readTimeout(5, TimeUnit.MINUTES)    // Tiempo máximo para leer respuesta
            .writeTimeout(5, TimeUnit.MINUTES)   // Tiempo máximo para enviar solicitud
            .addInterceptor(logging)
            .build()

        //HttpLoggingInterceptor es una clase que registra las solicitudes y respuestas HTTP.
        //level = BODY significa que se imprimirá el cuerpo
        //connectTimeout: Tiempo máximo para establecer la conexión con el servidor.
        //readTimeout: Tiempo máximo para leer la respuesta del servidor.
        //writeTimeout: Tiempo máximo para enviar la solicitud al servidor.
        //addInterceptor(logging): Agrega un interceptor HTTP que registra las solicitudes y respuestas HTTP.
        //build(): Construye el cliente OkHttpClient con las configuraciones especificadas.
    }

    fun getApi() : CheemsAPI? {
        val gson = GsonBuilder().create()
        val retrofit = Retrofit.Builder()
            .baseUrl("http://168.231.73.244:5000/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        return retrofit.create(CheemsAPI::class.java)
    }

    fun getApiWeather() : WeatherAPI? {
        val gson = GsonBuilder().create()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        return retrofit.create(WeatherAPI::class.java)
    }
}