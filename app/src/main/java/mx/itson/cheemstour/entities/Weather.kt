package mx.itson.cheemstour.entities

class Weather {
    var name: String? = null
    var timezone: Int = 0
    var main: Main? = null
    //var main: Main? = null representa el bloque "main": { ... } del JSON.
    var weather: List<WeatherDescription>? = null
    //var weather: List<WeatherDescription>? = null representa el arreglo "weather": [ { ... } ] del JSON.
}

class Main {
    var temp: Double = 0.0
    var feels_like: Double = 0.0
    var temp_min: Double = 0.0
    var temp_max: Double = 0.0
    var pressure: Int = 0
    var humidity: Int = 0
}

class WeatherDescription {
    var main: String? = null
    var description: String? = null
    var icon: String? = null
}
