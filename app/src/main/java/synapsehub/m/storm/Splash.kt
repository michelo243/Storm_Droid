package synapsehub.m.storm

import android.animation.Animator
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.animation.DynamicAnimation
import android.support.animation.SpringAnimation
import android.support.animation.SpringForce
import android.support.design.widget.Snackbar
import android.util.DisplayMetrics
import android.view.animation.DecelerateInterpolator
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_splash.*
import org.json.JSONArray
import synapsehub.m.storm.helper.StormDatabaseHelper
import synapsehub.m.storm.model.User
import org.json.JSONException
import synapsehub.m.storm.model.Customer
import synapsehub.m.storm.model.Price
import synapsehub.m.storm.model.ToastType
import synapsehub.m.storm.utils.MethodUtils
import synapsehub.m.storm.utils.PrefsUtils


class Splash : AppCompatActivity() {

    lateinit var springForce: SpringForce
    lateinit var sqlidb: StormDatabaseHelper
    private var Monsieur=User()
    private var Personne=User()
    private var Custom=Customer()
    private var MonPrix=Price()
    private var username:String=""
    private var password:String=""
    private var typeconso:String=""
    private var prix:String=""
    private var dateprix:String=""
    private var statusSyncPrice:Int=0
    private var valueBackdor:Int=0
    lateinit var sharedPreference: PrefsUtils


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        //database sqlite
        sqlidb = StormDatabaseHelper(applicationContext)
        sharedPreference = PrefsUtils(this)
        // replace the *** with the  name of the key
        if (sharedPreference.getStatusFirstSyncPrice("****")!=null) { 
            statusSyncPrice = sharedPreference.getStatusFirstSyncPrice("****")!!
        }



        Handler().postDelayed({
            //do stuff
            //Like your Background calls and all
            springForce = SpringForce(0f)
            relative_layout.pivotX = 0f
            relative_layout.pivotY = 0f
            val springAnim = SpringAnimation(relative_layout, DynamicAnimation.ROTATION).apply {
                springForce.dampingRatio = SpringForce.DAMPING_RATIO_HIGH_BOUNCY
                springForce.stiffness = SpringForce.STIFFNESS_VERY_LOW
            }
            springAnim.spring = springForce
            springAnim.setStartValue(80f)
            springAnim.addEndListener(object : DynamicAnimation.OnAnimationEndListener {
                override fun onAnimationEnd(animation: DynamicAnimation<out DynamicAnimation<*>>?, canceled: Boolean, value: Float, velocity: Float) {
                    val displayMetrics = DisplayMetrics()
                    windowManager.defaultDisplay.getMetrics(displayMetrics)
                    val height = displayMetrics.heightPixels.toFloat()
                    val width = displayMetrics.widthPixels
                    relative_layout.animate()
                        .setStartDelay(1)
                        .translationXBy(width.toFloat() / 2)
                        .translationYBy(height)
                        .setListener(object : Animator.AnimatorListener {
                            override fun onAnimationRepeat(p0: Animator?) {

                            }

                            override fun onAnimationEnd(p0: Animator?) {

                                //load users before doing anything else
                                if(isNetworkAvailable()){
                                    sqlidb.deleteFromUser()
                                    sqlidb.deleteFromCustomer()
                                    //load all users
                                    loadUsers()
                                    loadCustomers()
                                    //MethodUtils.makeToastMessage(applicationContext,"Genial",ToastType.Long)
                                }else{
                                    //affiche message de connection
                                    MethodUtils.makeToastMessage(applicationContext,"Erreur de connection a Internet",ToastType.Long)

                                }

                                //load all price from remote
                               // checkFirstStatusSync()
                                loadPrices()

                                val intent = Intent(applicationContext, MainActivity::class.java)
                                finish()
                                startActivity(intent)
                                overridePendingTransition(0, 0)
                            }

                            override fun onAnimationCancel(p0: Animator?) {

                            }

                            override fun onAnimationStart(p0: Animator?) {

                            }

                        })
                        .setInterpolator(DecelerateInterpolator(1f))
                        .start()
                }
            })
            springAnim.start()
        }, 5000)

    }

    private fun loadUsers() {

        val url=getString(R.string.url_getalllusers)

        val stringRequest = StringRequest(Request.Method.GET, url,
            Response.Listener { response ->
                try {
                    //converting the string to json array object
                    val array = JSONArray(response)

                    //traversing through all the object
                    for (i in 0 until array.length()) {

                        //getting product object from json array
                        val monuser = array.getJSONObject(i)

                        username=monuser.getString("username")
                        password=monuser.getString("password")
                        //adding the product to product list
                        Monsieur.username=username
                        Monsieur.password=password

                        sqlidb.UsersInsert(Monsieur)
                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { Toast.makeText(applicationContext,"Difficultes de chargements des utilisateurs !",Toast.LENGTH_LONG).show() })

        //adding our stringrequest to queue
        Volley.newRequestQueue(this).add(stringRequest)
    }

    private fun loadPrices() {

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
                            //replace the
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
            Response.ErrorListener { Toast.makeText(applicationContext,"Echec du Chargement des prix!",Toast.LENGTH_LONG).show() })

        //adding our stringrequest to queue
        Volley.newRequestQueue(this).add(stringRequest)
    }


    /**
     * Allow to check if Internet connection is okey
     */
    private fun isNetworkAvailable():Boolean{
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE)
        return if(connectivityManager is ConnectivityManager){
            val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
            networkInfo?.isConnected ?: false
        } else false
    }


    /*

     */
    private fun loadCustomers() {

        val url=getString(R.string.url_getcustomers)

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
            Response.ErrorListener { Toast.makeText(applicationContext,"echec du Chargement des clients !",Toast.LENGTH_LONG).show() })
        //adding our stringrequest to queue
        Volley.newRequestQueue(this).add(stringRequest)
    }

}
