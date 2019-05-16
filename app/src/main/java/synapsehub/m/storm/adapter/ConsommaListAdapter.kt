package synapsehub.m.storm.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.conso_row.view.*
import synapsehub.m.storm.R
import synapsehub.m.storm.model.Consommation

/**
 * Created by Michelo on 3/18/19 at 3:42 PM.
 * for project -> Storm_droid Copyright : SynapseHub
 */
class ConsommaListAdapter (ctx:Context, val consommas:ArrayList<Consommation>) : RecyclerView.Adapter<ConsommaListAdapter.ViewHolder>() {

    val ctx=ctx

    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): ConsommaListAdapter.ViewHolder {
        val v=LayoutInflater.from(p0.context).inflate(R.layout.conso_row,p0,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return consommas.size
    }

    override fun onBindViewHolder(holder: ConsommaListAdapter.ViewHolder, p1: Int) {
        val conso:Consommation=consommas[p1]
        val refcli=conso.ref_client
        val total:Int=conso.qte.toInt()*conso.prix
        val litres=" Litres."
        val monnaie= " Francs Congolais."

        holder.title.text=conso.ref_client
        holder.type.text=conso.type_conso
        holder.qtea.text=conso.qte + litres
        holder.paidco.text=total.toString() + monnaie
        holder.dateco.text=conso.date_cons

        holder.itemView.setOnClickListener{
            val affiche="Le client $refcli a acheter " + conso.qte + " litres equivalent a " + total.toString() + " Dollars US."
            Toast.makeText(ctx," $affiche", Toast.LENGTH_LONG).show()
        }

    }

    class ViewHolder(items: View) : RecyclerView.ViewHolder(items){
        val title=items.title
        val type=items.type
        val qtea=items.qtea
        val paidco=items.paidco
        val dateco=items.dateco
    }


}