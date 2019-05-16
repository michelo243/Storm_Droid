package synapsehub.m.storm.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import synapsehub.m.storm.StormActivity
import synapsehub.m.storm.helper.StormDatabaseHelper
import synapsehub.m.storm.interfaces.ISharedFragment
import synapsehub.m.storm.utils.PrefsUtils
import synapsehub.m.storm.MainActivity
import synapsehub.m.storm.R


/**
 * Created by Michelo on 2/8/19 at 1:55 AM.
 * for project -> Storm_droid Copyright : SynapseHub
 */
class LoginFragment :Fragment (), ISharedFragment {

    override fun openFragment(fragment: Fragment, fragment_id: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        lateinit var sqlidb:StormDatabaseHelper
        lateinit var mCCtx:Context
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view=LayoutInflater.from(container?.context).inflate(R.layout.fragment_login,container,false)

        mCCtx=container!!.context

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sqlidb= StormDatabaseHelper(mCCtx)
        val sharedPreference:PrefsUtils=PrefsUtils(mCCtx)


        /*val myfragment = AddSqliteTraiteurFragment.newInstance()
        val fragmentTransaction = fragmentManager!!.beginTransaction()
        fragmentTransaction.replace(R.id.framefragmenthome, myfragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()*/

        val btnL=view?.findViewById<View>(R.id.btnlogin) as Button
        val txtUN=view?.findViewById<View>(R.id.login_username) as EditText
        val txtPWD=view?.findViewById<View>(synapsehub.m.storm.R.id.login_password) as EditText


        btnL.setOnClickListener { view ->
            authenticateUser(txtUN.text.toString(),txtPWD.text.toString())
            //save to shareprefs
            sharedPreference.saveUserN("username",txtUN.text.toString())
           // sharedPreference.save("email",email)
        }



    }

    private fun authenticateUser(username: String, password: String) {
        //if (sqlidb.userCheck(username,password)) startActivity(Intent(mCCtx,StormActivity::class.java))
        if (sqlidb.userCheck(username,password)){
            val intent = Intent(mCCtx, StormActivity::class.java)
           // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)

        }
    }





}