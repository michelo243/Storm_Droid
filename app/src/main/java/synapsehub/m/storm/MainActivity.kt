package synapsehub.m.storm

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import synapsehub.m.storm.fragment.LoginFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //setSupportActionBar(toolBar)


        val tx = supportFragmentManager.beginTransaction()
      //  tx.replace(R.id.framefragment, QrScanFragment())
       // tx.replace(R.id.framefragment,LoginFragment())
        tx.replace(R.id.framefragment,LoginFragment())
        tx.commit()

    }




}
