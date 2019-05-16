package synapsehub.m.storm.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.AppCompatEditText
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import synapsehub.m.storm.interfaces.ISharedFragment
import android.widget.Button
import android.widget.EditText
import synapsehub.m.storm.R
import synapsehub.m.storm.helper.StormDatabaseHelper


/**
 * Created by Michelo on 2/17/19 at 12:28 AM.
 * for project -> Storm_droid Copyright : SynapseHub
 */
class RegisterFragment:Fragment(), ISharedFragment {

    companion object {
        lateinit var mCCtx: Context
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = LayoutInflater.from(container!!.context).inflate(R.layout.fragment_register,container,false)

        mCCtx=container!!.context

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnreg=view.findViewById<View>(R.id.btnregister) as Button
        val btngologin=view.findViewById<View>(R.id.btnr_login) as Button
        val txtname=view.findViewById<View>(R.id.r_name) as EditText
        val txtusername=view.findViewById<View>(R.id.r_username) as EditText
        val txtpassword=view.findViewById<View>(R.id.r_password) as EditText
        val txtemail=view.findViewById<View>(R.id.r_email) as EditText

        btngologin.setOnClickListener { view ->
            val fragment=LoginFragment()
            openFragment(fragment, R.id.framefragment)
        }


        btnreg.setOnClickListener { view ->
            insert_user()
        }
    }

    private fun insert_user() {

       // RestAdapter adapter=new RestAdapter

    }


    override fun openFragment(fragment: Fragment, fragment_id: Int) {
        fragmentManager!!.beginTransaction().replace(fragment_id, fragment).addToBackStack(null).commit()
    }



}