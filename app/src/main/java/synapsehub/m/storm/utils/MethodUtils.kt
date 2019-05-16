@file:Suppress("UNUSED_EXPRESSION")

package synapsehub.m.storm.utils

import android.content.Context
import android.support.annotation.AnimRes
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import synapsehub.m.storm.R
import synapsehub.m.storm.Storm_droidApplication
import synapsehub.m.storm.helper.StormDatabaseHelper
import synapsehub.m.storm.model.Consommation
import synapsehub.m.storm.model.Customer
import synapsehub.m.storm.model.Price
import synapsehub.m.storm.model.ToastType
import synapsehub.m.storm.ws.EndPoints
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.HashMap


class MethodUtils {

    companion object {

        lateinit var sqlidb: StormDatabaseHelper
        lateinit var sharedPreference: PrefsUtils
        var typeconso:String=""
        var prix:String=""
        var dateprix:String=""
        var statusSyncPrice:Int=0
        var MonPrix= Price()
        var Custom= Customer()

        fun AppCompatActivity.replaceFragmentSafely(fragment: Fragment,
                                                           tag: String,
                                                           allowStateLoss: Boolean = false,
                                                           @IdRes containerViewId: Int,
                                                           @AnimRes enterAnimation: Int = 0,
                                                           @AnimRes exitAnimation: Int = 0,
                                                           @AnimRes popEnterAnimation: Int = 0,
                                                           @AnimRes popExitAnimation: Int = 0) {
            val ft = supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(enterAnimation, exitAnimation, popEnterAnimation, popExitAnimation)
                .replace(containerViewId, fragment, tag)
            if (!supportFragmentManager.isStateSaved) {
                ft.commit()
            } else if (allowStateLoss) {
                ft.commitAllowingStateLoss()
            }
        }


        /**
         * Allow to custom message to be set in a Toast
         */
        fun makeToastMessage(context: Context, strMessage: String, duration: ToastType){
            when(duration){
                ToastType.Long -> Toast.makeText(context, strMessage, Toast.LENGTH_LONG).show()
                ToastType.Short -> Toast.makeText(context, strMessage, Toast.LENGTH_SHORT).show()
            }
        }

        /**
         * Allow to show the date in a certain format
         */
        fun showShortDate(dateStr: String): String {
            try {
                val fmt = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                val date = fmt.parse(dateStr)
                val fmtOut = SimpleDateFormat("MMM d, yyyy")
                return fmtOut.format(date)
            } catch (e: ParseException) {
                //We can log this exception in real app
                return ""
            }
        }

        fun syncFuelToRemote(context: Context, buff:Consommation ){
            //creating volley string request
            val stringRequest = object : StringRequest(
                Request.Method.POST, EndPoints.URL_ADD_CONSOMMATION,
                Response.Listener<String> { response ->
                    try {
                        val obj = JSONObject(response)
                        Toast.makeText(context!!, obj.getString("message"), Toast.LENGTH_LONG).show()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener { volleyError -> Toast.makeText(context!!, volleyError.message, Toast.LENGTH_LONG).show() }) {
                @Throws(AuthFailureError::class)
                override fun getParams(): Map<String, String> {
                    val params = HashMap<String, String>()
                    //val price=buff.prix.toString()
                    params.put("ref_client", buff.ref_client)
                    params.put("type_cons", buff.type_conso)
                    params.put("qte", buff.qte)
                    params.put("prix", buff.prix.toString())
                    return params
                }
            }
            //adding request to queue
            Storm_droidApplication.instance?.addToRequestQueue(stringRequest)
        }


        fun syncPriceFromRemote(context: Context) {

            sqlidb = StormDatabaseHelper(context)
            sharedPreference = PrefsUtils(context)
            if (sharedPreference.getStatusFirstSyncPrice("***")!=null) {
                statusSyncPrice = sharedPreference.getStatusFirstSyncPrice("***")!!
            }

            val url="url_for_price.php"

            val stringRequest = StringRequest(Request.Method.GET, url,
                Response.Listener { response ->
                    try {
                        //converting the string to json array object
                        val array = JSONArray(response)

                        //traversing through all the object
                        for (i in 0 until array.length()) {
                            //getting product object from json array
                            val monprice = array.getJSONObject(i)

                            typeconso=monprice.getString("typeconso")
                            prix=monprice.getString("prix")
                            dateprix=monprice.getString("dateprix")

                            //adding to database
                            MonPrix.typeconso=typeconso
                            MonPrix.prix=prix.toInt()
                            MonPrix.dateprix=dateprix

                            // sqlidb.insertPrice(MonPrix)
                            if(statusSyncPrice==0){
                                //sqlidb.deleteFromPrice()
                                // loadPrices()
                                sqlidb.insertPrice(MonPrix)
                                //the key
                                sharedPreference.statusFirstSyncPrice("***",1)
                            }else if(statusSyncPrice==1){
                                //loadPrices()
                                sqlidb.updatePrice(MonPrix)
                            }
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener { Toast.makeText(context,"The price sync didn't work!",Toast.LENGTH_LONG).show() })
            //adding our stringrequest to queue
            Volley.newRequestQueue(context).add(stringRequest)
        }


        fun syncCustomersFromRemote(context: Context) {

            val url="url_for_customers.php"

            val stringRequest = StringRequest(Request.Method.GET, url,
                Response.Listener { response ->
                    try {
                        //converting the string to json array object
                        val array = JSONArray(response)
                        //traversing through all the object
                        for (i in 0 until array.length()) {
                            //getting product object from json array
                            val client = array.getJSONObject(i)
                            //adding to list
                            Custom.matr_client=client.getString("matr_client")
                            Custom.nom_client=client.getString("nom")
                            sqlidb.CustomersInsert(Custom)
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener { Toast.makeText(context,"Customer sync didn't work!",Toast.LENGTH_LONG).show() })
            //adding our stringrequest to queue
            Volley.newRequestQueue(context).add(stringRequest)
        }



        //end of companion object
    }



}