package synapsehub.m.storm.model

/**
 * Created by Michelo on 2/8/19 at 1:12 AM.
 * for project -> Storm_droid Copyright : SynapseHub
 */
class Consommation {

    var id:Int=0
    var qte:String=""
    var date_cons:String=""
    var ref_client:String=""
    var type_conso:String=""
    var prix:Int=0
    var username:String=""
    var sync_status:Int=0

    constructor() // null constructor


    constructor(qte: String, date_cons: String,ref_client:String, type_conso:String, prix:Int, username:String ) {
        this.qte = qte
        this.date_cons = date_cons
        this.ref_client=ref_client
        this.type_conso=type_conso
        this.prix=prix
        this.username=username
        //this.sync_status=sync_status
    }


}