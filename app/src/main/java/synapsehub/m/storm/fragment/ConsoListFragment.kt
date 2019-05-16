@file:Suppress("UNUSED_EXPRESSION")

package synapsehub.m.storm.fragment

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import synapsehub.m.storm.R
import synapsehub.m.storm.adapter.ConsommaListAdapter
import synapsehub.m.storm.helper.StormDatabaseHelper
import synapsehub.m.storm.model.ToastType
import synapsehub.m.storm.utils.MethodUtils

/**
 * Created by Michelo on 3/18/19 at 4:04 PM.
 * for project -> Storm_droid Copyright : SynapseHub
 */
class ConsoListFragment : Fragment() {

    companion object {
        lateinit var sqlidb: StormDatabaseHelper
        lateinit var mCtx: Context
        //lateinit val adapter
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = LayoutInflater.from(container?.context).inflate(R.layout.consom_list_activity, container, false)

        mCtx = container!!.context

        return v
    }

    private fun viewAllConsoNotYetSynchro() {
        val consomaList = sqlidb.get_AllConsoForSynchronisation(mCtx)
        val adapter = ConsommaListAdapter(mCtx, consomaList)
        val rv: RecyclerView = view?.findViewById<View>(R.id.rvconsomma) as RecyclerView

        rv.layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)

        rv.adapter = adapter
        //update of recyclerview list
        adapter.notifyDataSetChanged()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sqlidb = StormDatabaseHelper(mCtx)

        val btsync = view.findViewById<View>(R.id.btn_synchro) as Button

        viewAllConsoNotYetSynchro()

        btsync.setOnClickListener { view ->
            val cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = cm.activeNetworkInfo
            if (activeNetwork != null) {
                //if connected to wifi or mobile data plan
                if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                       //Synchronise data
                        sqlidb.go_InsertUnSyncedConsomma(mCtx)
                   // adapter.notifyDataSetChanged();
                }
            }else{
               // MethodUtils.makeToastMessage(context!!,"Aucune connection internet detectee !!", ToastType.Long )

                Snackbar.make(view, getString(R.string.nointernet),
                    Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            }
        }
    }







}