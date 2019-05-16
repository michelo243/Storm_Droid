package synapsehub.m.storm

import android.app.Application
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

/**
 * Created by Michelo on 2/8/19 at 1:26 AM.
 * for project -> Storm_droid Copyright : SynapseHub
 */
class Storm_droidApplication: Application() {



    companion object {
        private val TAG=Storm_droidApplication::class.java.simpleName
        @get:Synchronized var instance:Storm_droidApplication?=null

        private set
    }

    //cree une instance de l'application
    override fun onCreate() {
        super.onCreate()
        instance=this

    }

    val requestQueue: RequestQueue?=null
    get(){
        if(field==null){
            return Volley.newRequestQueue(applicationContext)
        }
        return field
    }

    fun<T> addToRequestQueue(request:Request<T>){
        request.tag= TAG
        requestQueue?.add(request)
    }


}