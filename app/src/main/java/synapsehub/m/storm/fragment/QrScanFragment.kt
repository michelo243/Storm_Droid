package synapsehub.m.storm.fragment


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteTableLockedException
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.AppCompatEditText
import android.support.v7.widget.AppCompatSpinner
import android.support.v7.widget.AppCompatTextView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import synapsehub.m.storm.interfaces.ISharedFragment
import kotlinx.android.synthetic.main.qrcode_fragment.*
import org.json.JSONException
import org.json.JSONObject
import synapsehub.m.storm.R
import synapsehub.m.storm.Storm_droidApplication
import synapsehub.m.storm.communicator.Communicator
import synapsehub.m.storm.helper.FieldsContrats
import synapsehub.m.storm.helper.StormDatabaseHelper
import synapsehub.m.storm.model.Consommation
import synapsehub.m.storm.model.FragmentTagValue
import synapsehub.m.storm.model.QrCodeData
import synapsehub.m.storm.model.User
import synapsehub.m.storm.utils.PrefsUtils
import synapsehub.m.storm.ws.EndPoints
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


/**
 * Created by Michelo on 2/10/19 at 10:56 AM.
 * for project -> Storm_droid Copyright : SynapseHub
 */

class QrScanFragment: Fragment(), ISharedFragment {

    //    private var ctx: Context? = null
    private var sqliteDB: StormDatabaseHelper? = null
    private var qrcodeDataObject = QrCodeData()
    private var fuelConsoData=Consommation()
    private var leprix:Int=0
    private var texteAtester:String=""
    private var current_user:String=""

    //Default value for QrCode Scanned
    private var stringValue: String = "QrCode Has not been scanned !!"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.qrcode_fragment, container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Initialise SqliteDBHelper object
        sqliteDB = StormDatabaseHelper(context!!)

        val sharedPreference: PrefsUtils = PrefsUtils(context!!)
        //get from SharedPreferences
        if (sharedPreference.getValueUserN("username")!=null) {
            var UsernameString = sharedPreference.getValueUserN("username")!!
            current_user=UsernameString
        }



        val fuelQte=view.findViewById<View>(R.id.fuel_qte) as EditText
        val fuel_Prix=view.findViewById<View>(R.id.fuel_prix) as EditText
        val type_cons=view.findViewById<View>(R.id.spinner_type_cons) as Spinner
        view.findViewById<View>(R.id.qr_name) as TextView

        fuel_Prix.isEnabled = false

        type_cons?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // ??
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var carburant=spinner_type_cons?.selectedItem.toString()
                leprix = sqliteDB!!.getPriceByConso(carburant)
                fuel_Prix.setText(leprix.toString())
            }
        }


        //Set FragmentCaptureMain value
        FieldsContrats.FRAGMENT_VALUE_TAG = FragmentTagValue.CaptureMain

        qr_name.text = stringValue

        //Perform Action when clic on capture button
        btnScan.setOnClickListener {
            val fragmentCapture = FragmentCapture()
            openFragment(fragmentCapture, R.id.framefragmenthome)
        }

        //Using Shared Data througt ViewModel Class
        val model = ViewModelProviders.of(activity!!).get(Communicator::class.java)
        model.modelMessage.observe(this, Observer<Any> { t ->
            if(t.toString().isNotEmpty()) {
                qr_name.text = t.toString()
                texteAtester=t.toString()
            }
        })


        //Perform Action when saving sacanned QrCode
        btn_save_fuel.setOnClickListener {
            //Save captured QRCode
            try {
                if(qr_name.text.toString().isNotEmpty() && qr_name.text.toString() != stringValue && fuel_qte.text.toString().toInt()>0 && qr_name.text.toString() != getString(R.string.empty)){

                    qrcodeDataObject.qrcodeContent = qr_name.text.toString()

                    val refclient = qr_name?.text.toString()
                    val typecons = spinner_type_cons?.selectedItem.toString()
                    val fuelqte=fuel_qte?.text.toString()
                    val fuelprix=fuel_prix?.text.toString()


                    //Set Date and Time using difference way according Sdk Min Version
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                        val currentDateTime = LocalDateTime.now()
                        val formatterDate = DateTimeFormatter.ofPattern("dd-MM-YYYY")
                        val formatterTime = DateTimeFormatter.ofPattern("HH:mm:ss")

                        qrcodeDataObject.strDate = currentDateTime.format(formatterDate)
                        qrcodeDataObject.strTime = currentDateTime.format(formatterTime)

                        /*
                        allow to insert only recognized customers
                         */
                       // if(sqliteDB!!.customerCheck(qr_name.text.toString())){

                        if(sqliteDB!!.customerCheck(texteAtester.trim())){

                            //for Consommation
                            fuelConsoData.ref_client=refclient
                            fuelConsoData.qte=fuelqte
                            fuelConsoData.prix=fuelprix.toInt()
                            fuelConsoData.type_conso=typecons
                            fuelConsoData.date_cons=currentDateTime.format(formatterDate)
                            fuelConsoData.username=current_user


                            if(isNetworkAvailable()){
                                //to remote database
                                addFuelToDatabase()
                            }else{
                                //save the fuel consommation
                                sqliteDB!!.saveConsommationTolocalDatabase(fuelqte,currentDateTime.format(formatterDate),refclient,typecons,fuelprix.toInt(),current_user,0)
                            }

                            //clear Text
                            fuel_Prix.text.clear()
                            fuelQte.text.clear()
                            qr_name.text = ""
                            qr_name.text = stringValue

                        } else {
                           // MethodUtils.makeToastMessage(context!!,getString(R.string.savingcustomererror), ToastType.Long )

                            Snackbar.make(view, getString(R.string.savingcustomererror),
                                Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show()
                        }


                    }else{
                        val currentDateTime = Date()
                        val formatterDate = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
                        val formatterTime = SimpleDateFormat("HH:mm:ss", Locale.FRENCH)

                        qrcodeDataObject.strDate = formatterDate.format(currentDateTime)
                        qrcodeDataObject.strTime = formatterTime.format(currentDateTime)

                        if(sqliteDB!!.customerCheck(texteAtester.trim())){
                            //for Consommation
                            fuelConsoData.ref_client=refclient
                            fuelConsoData.qte=fuelqte
                            fuelConsoData.prix=fuelprix.toInt()
                            fuelConsoData.type_conso=typecons
                            fuelConsoData.date_cons=formatterDate.format(currentDateTime)
                            fuelConsoData.username=current_user


                            if(isNetworkAvailable()){
                                //to remote database
                                addFuelToDatabase()

                                Snackbar.make(view, "Consommation enregistree !",
                                    Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show()
                            }else{
                                //save the fuel consommation
                                //sqliteDB!!.saveFuelTolocalDatabase(fuelConsoData,0)
                                sqliteDB!!.saveConsommationTolocalDatabase(fuelqte,formatterDate.format(currentDateTime),refclient,typecons,fuelprix.toInt(),current_user,0)
                                // saveFuelUsage(fuelConsoData)
                                // var strdata:String=refclient+ " " + typecons + " " + fuelqte + " " + fuelprix
                            }

                            //clear Text
                            fuel_Prix.text.clear()
                            fuelQte.text.clear()
                            qr_name.text=""
                            qr_name.text = stringValue

                        }else {

                            Snackbar.make(view, getString(R.string.savingcustomererror),
                                Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show()
                        }
                    }
                   // MethodUtils.makeToastMessage(context!!,"Enregistrement reussi.", ToastType.Long )
                }else{
                   // MethodUtils.makeToastMessage(context!!,getString(R.string.valeursnulles), ToastType.Long )
                    Snackbar.make(view, getString(R.string.valeursnulles),
                        Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show()
                }
            }catch (e: SQLiteTableLockedException){
               // MethodUtils.makeToastMessage(context!!,getString(R.string.sqlitetablelockedexception), ToastType.Long )
                Snackbar.make(view, getString(R.string.sqlitetablelockedexception),
                    Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            }catch (e: SQLiteException){
               // MethodUtils.makeToastMessage(context!!,getString(R.string.sqliteexception), ToastType.Long )
                Snackbar.make(view, getString(R.string.sqliteexception),
                    Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            }catch (e:Exception) {
               // MethodUtils.makeToastMessage(context!!, getString(R.string.savingerror), ToastType.Long)
                Snackbar.make(view, getString(R.string.savingerror),
                    Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            }
        }

    }


    /*private fun validateCustomer(matricule: String) :String {

        if (sqliteDB!!.customerCheck(matricule)) return
    }*/


    private fun saveQRCodeCaptured() {
        sqliteDB!!.insertQrCode(qrcodeDataObject)
    }

    private fun saveFuelUsage(x:Consommation) {
        //if is 0 mean this need to be Sync
        //if is 1 mean already sync
        sqliteDB!!.saveFuelTolocalDatabase(x,0)
    }

    override fun openFragment(fragment: Fragment, fragment_id: Int) {
        fragmentManager?.beginTransaction()?.replace(fragment_id, fragment)?.commit()
    }

    override fun onResume() {
        super.onResume()

        //Set FragmentCaptureMain value
        FieldsContrats.FRAGMENT_VALUE_TAG = FragmentTagValue.CaptureMain
    }


    //adding new consommation in the remote database
    private fun addFuelToDatabase(){

        val refclient = qr_name?.text.toString()
        val typecons = spinner_type_cons?.selectedItem.toString()
        val fuelqte=fuel_qte?.text.toString()
        val fuelprix=fuel_prix?.text.toString()
        val username=current_user

        //creating volley string request
        val stringRequest = object : StringRequest(Request.Method.POST, EndPoints.URL_ADD_CONSOMMATION,
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
                params.put("ref_client", refclient)
                params.put("type_cons", typecons)
                params.put("qte", fuelqte)
                params.put("prix", fuelprix)
                params.put("username", username)
                return params
            }
        }

        //adding request to queue
        Storm_droidApplication.instance?.addToRequestQueue(stringRequest)

    }

    fun getPriceFromDb(){
        var carburant=spinner_type_cons?.selectedItem.toString()
        leprix = sqliteDB!!.getPriceByConso(carburant)
        fuel_prix.setText(leprix.toString())

    }

    /**
     * Allow to check if Internet connection is okey
     */
    private fun isNetworkAvailable():Boolean{
        val connectivityManager = activity?.getSystemService(Context.CONNECTIVITY_SERVICE)
        return if(connectivityManager is ConnectivityManager){
            val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
            networkInfo?.isConnected ?: false
        } else false
    }


}