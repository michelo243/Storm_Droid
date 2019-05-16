package synapsehub.m.storm.model

/**
 * Created by Michelo on 2/8/19 at 1:09 AM.
 * for project -> Storm_droid Copyright : SynapseHub
 */
class User {

    var id:Int=0
    var username:String=""
    var password:String=""



    //null constructor
    constructor()

    constructor(username: String, password: String) {
        this.username = username
        this.password = password
    }

}