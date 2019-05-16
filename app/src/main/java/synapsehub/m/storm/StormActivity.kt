package synapsehub.m.storm

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_storm.*
import kotlinx.android.synthetic.main.app_bar_storm.*
import synapsehub.m.storm.fragment.*
import synapsehub.m.storm.helper.StormDatabaseHelper
import synapsehub.m.storm.model.ToastType
import synapsehub.m.storm.utils.MethodUtils
import synapsehub.m.storm.utils.MethodUtils.Companion.replaceFragmentSafely
import synapsehub.m.storm.utils.PrefsUtils



class StormActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    companion object {

        lateinit var sqlidb: StormDatabaseHelper
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_storm)
        setSupportActionBar(toolbar)
        
      //  val nameheader=findViewById<View>(R.id.textvheader) as TextView

        setActionBarTitle("Storm Rdc")

        /*fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

*/

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        val sharedPreference: PrefsUtils = PrefsUtils(this)
        //get from SharedPreferences
        if (sharedPreference.getValueUserN("username")!=null) {
            var UsernameString = sharedPreference.getValueUserN("username")!!
           // Toast.makeText(this@StormActivity,UsernameString, Toast.LENGTH_SHORT).show()
            //nameheader.text=UsernameString

            setUserNameDrawer(UsernameString)
        }

        val tx = supportFragmentManager.beginTransaction()
        tx.replace(R.id.framefragmenthome, QrScanFragment())
        tx.commit()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
           // super.onBackPressed()
            return
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.storm, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_conso -> {
                // Handle the camera action
                //definir un TAG pour le fragment
                val TAG_FRAGMENT_TWO="Deuxieme"

                replaceFragmentSafely(
                    fragment = QrScanFragment(),
                    tag = TAG_FRAGMENT_TWO,
                    containerViewId = R.id.framefragmenthome,
                    allowStateLoss = true
                )
                setActionBarTitle("Consommation")
            }
            R.id.nav_data -> {
                val TAG_FRAGMENT_TWO="data"

                replaceFragmentSafely(
                    fragment = ConsoListFragment(),
                    tag = TAG_FRAGMENT_TWO,
                    containerViewId = R.id.framefragmenthome,
                    allowStateLoss = true
                )
                setActionBarTitle("Donnees du jour")
            }
            R.id.nav_price -> {
                val TAG_FRAGMENT_TWO="Prix"

                replaceFragmentSafely(
                    fragment = ListPricesFragment(),
                    tag = TAG_FRAGMENT_TWO,
                    containerViewId = R.id.framefragmenthome,
                    allowStateLoss = true
                )
                setActionBarTitle("Liste des prix")
            }
            R.id.nav_sync -> {

                val cm = applicationContext?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val activeNetwork = cm.activeNetworkInfo
                if (activeNetwork != null) {
                    //if connected to wifi or mobile data plan
                    if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                        //Synchronise data
                        //ConsoListFragment.sqlidb.go_InsertUnSyncedConsomma(ConsoListFragment.mCtx)
                        sqlidb = StormDatabaseHelper(applicationContext)
                        sqlidb.go_InsertUnSyncedConsomma(applicationContext)
                        MethodUtils.syncPriceFromRemote(applicationContext)
                        MethodUtils.syncCustomersFromRemote(applicationContext)
                    }
                }else{
                    MethodUtils.makeToastMessage(applicationContext!!,"Aucune connection internet detectee !!", ToastType.Long )
                }
            }

            R.id.nav_setting -> {
                val TAG_FRAGMENT_TWO="A Propos de..."

                replaceFragmentSafely(
                    fragment = FragmentAbout(),
                    tag = TAG_FRAGMENT_TWO,
                    containerViewId = R.id.framefragmenthome,
                    allowStateLoss = true
                )
                setActionBarTitle("A Propos de nous ...")
            }

        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }



    fun setActionBarTitle(title: String) {
        supportActionBar!!.title = title
    }

    fun setUserNameDrawer(username:String)
    {
        val navigationView = findViewById<View>(R.id.nav_view) as NavigationView

        val headerView = navigationView.getHeaderView(0)
        val drawerImage = headerView.findViewById(R.id.imageView) as ImageView
        val drawerUsername = headerView.findViewById(R.id.textvusern) as TextView
        val drawerAccount = headerView.findViewById(R.id.textvheader) as TextView
        //drawerImage.setImageDrawable(R.drawable.ic_storm_v3)
        drawerUsername.text = username
        drawerAccount.text = resources.getText(R.string.nav_header_subtitle)

        /*View headerView=navigationView.getHeaderView(0);

        TextView nameTv=(TextView)headerView.findViewById(R.id.tvHeaderView);
        nameTv.setText("any string")*/
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            super.onKeyDown(keyCode, event)
            return true
        }
        return false
    }

    override fun onResume() {
        // 1
        super.onResume()
        print("onResume")

    }

    override fun onPause() {
        // 4
        super.onPause()
        print("onPause")
    }


}
