package synapsehub.m.storm.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo


class NetWorkConection {

    companion object {
        @SuppressLint("MissingPermission")
        fun isNEtworkConnected(context: Context): Boolean {

            var connectionManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectionManager.activeNetworkInfo

            return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting
        }
    }
}

/* use like this

if (NetWorkConection.isNEtworkConnected(applicationContext)) {
    val inent = Intent(this@FirstScreen, MainActivity::class.java)
    startActivity(inent)
} else {
    toast("Please turn on your Internet")
}
*/
