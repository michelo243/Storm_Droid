package synapsehub.m.storm.model

/**
 * Created by Michelo on 2/8/19 at 1:09 AM.
 * for project -> Storm_droid Copyright : SynapseHub
 */
class Customer {

    var id:Int=0
    var matr_client:String=""
    var nom_client:String=""


    //null constructor
    constructor()

    constructor(matr_client: String, nom_client:String) {
        this.matr_client = matr_client
        this.nom_client=nom_client
    }

}