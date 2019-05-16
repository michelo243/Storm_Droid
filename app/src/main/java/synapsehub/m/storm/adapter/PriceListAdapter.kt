package synapsehub.m.storm.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.price_item_row.view.*
import synapsehub.m.storm.R
import synapsehub.m.storm.model.Price

/**
 * Created by Michelo on 3/15/19 at 3:54 PM.
 * for project -> Storm_droid Copyright : SynapseHub
 */
class PriceListAdapter(ctx:Context, val prices:ArrayList<Price>) : RecyclerView.Adapter<PriceListAdapter.ViewHolder>() {

    val ctx=ctx

    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): ViewHolder {
        val v= LayoutInflater.from(p0.context).inflate(R.layout.price_item_row,p0,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return prices.size
    }

    override fun onBindViewHolder(holder: PriceListAdapter.ViewHolder, p1: Int) {
        val price:Price=prices[p1]
        holder.txtprix.text=price.prix.toString()
        holder.txtdate.text=price.dateprix.toString()
        holder.txttype.text=price.typeconso.toString()

        holder.itemView.setOnClickListener{
            val affiche=price.typeconso + " = " + price.prix.toString()
            Toast.makeText(ctx," Le prix de $affiche",Toast.LENGTH_LONG).show()
        }
    }

    class ViewHolder(items:View) : RecyclerView.ViewHolder(items){
        val txtprix=items.tvprice
        val txtdate=items.tvdatep
        val txttype=items.tvproduct
    }

}