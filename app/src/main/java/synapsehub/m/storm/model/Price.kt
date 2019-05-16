package synapsehub.m.storm.model

/**
 * Created by Michelo on 3/7/19 at 2:44 AM.
 * for project -> Storm_droid Copyright : SynapseHub
 */
class Price {

    var id:Int=0
    var typeconso:String=""
    var prix:Int=0
    var dateprix:String=""

    constructor()

    constructor(typeconso: String, prix: Int) {
        this.typeconso = typeconso
        this.prix = prix
    }

    constructor(id: Int, typeconso: String, prix: Int, dateprix: String) {
        this.id = id
        this.typeconso = typeconso
        this.prix = prix
        this.dateprix = dateprix
    }


}