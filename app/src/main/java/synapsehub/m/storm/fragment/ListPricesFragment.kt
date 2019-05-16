package synapsehub.m.storm.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import synapsehub.m.storm.R
import synapsehub.m.storm.adapter.PriceListAdapter
import synapsehub.m.storm.helper.StormDatabaseHelper

/**
 * Created by Michelo on 3/15/19 at 4:38 PM.
 * for project -> Storm_droid Copyright : SynapseHub
 */

class ListPricesFragment:Fragment() {

    companion object {
        lateinit var sqlidb:StormDatabaseHelper
        lateinit var mCtx: Context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v= LayoutInflater.from(container?.context).inflate(R.layout.price_list_activity,container, false)

        mCtx=container!!.context

        return v
    }

    private fun viewAllPrices(){
        val pricelist= sqlidb.get_AllPrices(mCtx)
        val adapter= PriceListAdapter(mCtx,pricelist)
        val rv: RecyclerView =view?.findViewById<View>(R.id.rvprice) as RecyclerView
        rv.layoutManager= LinearLayoutManager(context, LinearLayout.VERTICAL,false) as RecyclerView.LayoutManager

        rv.adapter=adapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sqlidb = StormDatabaseHelper(mCtx)

        //affiche la lliste dans le recyclerview
        viewAllPrices()
    }

}