package mx.itson.cheemstour.entities

class Trip {

    constructor()

    constructor(id: Int, name : String, city : String, country : String, latitude: Double, longitude: Double){
        this.id = id
        this.name = name
        this.city = city
        this.country = country
        this.latitude = latitude
        this.longitude = longitude
    }

    var id: Int? = null
    var name:String? = null
    var city:String? = null
    var country:String? = null
    var latitude:Double = 0.0
    var longitude:Double = 0.0

}