package synapsehub.m.storm.utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

/**
 * Created by Michelo on 3/7/19 at 3:14 AM.
 * for project -> Storm_droid Copyright : SynapseHub
 */
class PrefsUtils (context: Context) {

    private val PREFS_NAME = "****" //prefname here aas it will be added to the phone
    val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun saveUserN(KEY_USERN: String, text: String) {

        val editor: SharedPreferences.Editor = prefs.edit()
        editor.putString(KEY_USERN, text)
        editor!!.commit()
    }

    fun saveBackdorUser(key_backdor:String, value:Int){
        val editor:SharedPreferences.Editor=prefs.edit()
        editor.putInt(key_backdor,value)
        editor!!.commit()
    }

    fun getBackdorUser(key_backdor: String):Int{
        return prefs.getInt(key_backdor,0)
    }

    fun statusFirstSyncPrice(key_price:String, value: Int){
        val editor:SharedPreferences.Editor=prefs.edit()
        editor.putInt(key_price,value)
        editor!!.commit()
    }

    fun save(KEY_NAME: String, value: Int) {
        val editor: SharedPreferences.Editor = prefs.edit()

        editor.putInt(KEY_NAME, value)

        editor.commit()
    }


    fun getValueUserN(KEY_USERN: String): String? {

        return prefs.getString(KEY_USERN, null)
    }

    fun getValueInt(KEY_NAME: String): Int {

        return prefs.getInt(KEY_NAME, 0)
    }

    fun getStatusFirstSyncPrice(key_price: String):Int{
        return prefs.getInt(key_price,0)
    }



    fun clearSharedPreference() {

        val editor: SharedPreferences.Editor = prefs.edit()

        //sharedPref = PreferenceManager.getDefaultSharedPreference s(context);

        editor.clear()
        editor.commit()
    }

    fun removeValue(KEY_NAME: String) {

        val editor: SharedPreferences.Editor = prefs.edit()

        editor.remove(KEY_NAME)
        editor.commit()
    }


}