package synapsehub.m.storm.helper

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast
import synapsehub.m.storm.model.*
import synapsehub.m.storm.utils.MethodUtils
import java.sql.SQLException

/**
 * Created by Michelo on 2/8/19 at 1:15 AM.
 * for project -> Storm_droid Copyright : SynapseHub
 */

class StormDatabaseHelper:SQLiteOpenHelper {

    companion object {
        // nothing for now
    }

       private var myContext:Context

    constructor(context: Context) : super(context, FieldsContrats.StormDD, null, 1) {
        this.myContext = context
    }


    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(FieldsContrats.Create_Table_User)
        db?.execSQL(FieldsContrats.Create_Table_Customer)
        db?.execSQL(FieldsContrats.Create_Table_Price)
        db?.execSQL(FieldsContrats.Table_Conso_Create)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
       try {
           db?.execSQL(FieldsContrats.DropTable_Customer)
           db?.execSQL(FieldsContrats.DropTable_User)
           db?.execSQL(FieldsContrats.DropTable_Conso)
           db?.execSQL(FieldsContrats.DropTable_Price)
           onCreate(db)
       }catch (e:SQLException){
           //.....
       }

    }

    fun insertPrice(price:Price){
        val db=this.writableDatabase
        val cv=ContentValues()
        cv.put(FieldsContrats.price_typeconso,price.typeconso)
        cv.put(FieldsContrats.price_prix,price.prix)
        cv.put(FieldsContrats.price_date,price.dateprix)

        db.insert(FieldsContrats.TablePrice, null, cv)
        db.close()
    }

    //get the price of typeconso
    fun getPriceByConso(carburant:String):Int{
        var leprix=0
        val db=readableDatabase
        val query="SELECT prix FROM ${FieldsContrats.TablePrice} WHERE typeconso='" + carburant +"'"
        val cursor=db.rawQuery(query,null)
        if(cursor != null){
            if(cursor.moveToNext()){
                do{
                    var valprice=cursor.getInt(cursor.getColumnIndex(FieldsContrats.price_prix))
                    leprix=valprice
                }while (cursor.moveToNext())
            }
        }
        cursor.close()
        db.close()
        return leprix
    }

    //update data in Price table
    fun updatePrice(prix:Price){
        val db=this.writableDatabase
        val values=ContentValues()

        values.put(FieldsContrats.price_typeconso,prix.typeconso)
        values.put(FieldsContrats.price_prix,prix.prix)
        values.put(FieldsContrats.price_date,prix.dateprix)

        val retVal=db.update(FieldsContrats.TablePrice,values, "typeconso = '" + prix.typeconso + "'", null)
        if(retVal>=1){
            Log.v("@@error_price","mise a jour du prix a reussie")
        }else{
            Log.v("@@error_price","mise a jour du prix a echouer")
        }
        db.close()

    }


    fun UsersInsert(personne: User){
        val db=this.writableDatabase
        val cv= ContentValues()
        cv.put(FieldsContrats.username,personne.username)
        cv.put(FieldsContrats.password,personne.password)

        db.insert(FieldsContrats.TableUser,null,cv)
        db.close()
    }

    fun CustomersInsert(custom:Customer){
        val db=this.writableDatabase
        val cv=ContentValues()
        cv.put(FieldsContrats.customer_matricule,custom.matr_client)
        cv.put(FieldsContrats.customer_name,custom.nom_client)

        db.insert(FieldsContrats.TableCustomer,null,cv)
        db.close()

    }

    fun customerCheck(matricule:String): Boolean{
        val db=this.writableDatabase
        val qry="select * from ${FieldsContrats.TableCustomer} where matr_client =  '" + matricule + "'"

        val cursor=db.rawQuery(qry,null)
        if(cursor.count<=0){
            cursor.close()
            return false
        }
        cursor.close()
        return true
    }

    fun deleteFromCustomer(){
        val db=this.writableDatabase
        db.execSQL("DELETE FROM ${FieldsContrats.TableCustomer}")
        db.close()
    }

    fun userCheck(username:String, password:String): Boolean{
        val db=this.writableDatabase
        val qry="select * from ${FieldsContrats.TableUser} where username = '" + username + "'" + " and password =  '" + password + "'"

        val cursor=db.rawQuery(qry,null)
        if(cursor.count<=0){
            cursor.close()
            return false
        }
        cursor.close()
        return true
    }

    fun deleteFromUser(){
        val db = this.writableDatabase
        db.execSQL("DELETE FROM ${FieldsContrats.TableUser}") //delete all rows in a table
        db.close()
    }

    fun deleteFromPrice(){
        val db = this.writableDatabase
        db.execSQL("DELETE FROM ${FieldsContrats.TablePrice}") //delete all rows in a table
        db.close()
    }

    fun deleteFromConsomma(id:Int){
        val db = this.writableDatabase
        db.execSQL("DELETE FROM ${FieldsContrats.TableConsommation} where sync_status=0 and id=$id") //delete all rows in a table
        db.close()
    }




    /* to readapte
    ------------------------------------------------------------------------------------------------------------------------------------------
     */

    fun saveFuelTolocalDatabase(conso:Consommation, sync_status: Int) {
        val db=this.writableDatabase
        val cv = ContentValues()
        cv.put(FieldsContrats.conso_qte,conso.qte)
        cv.put(FieldsContrats.conso_date,conso.date_cons)
        cv.put(FieldsContrats.conso_ref_client,conso.ref_client)
        cv.put(FieldsContrats.conso_type,conso.type_conso)
        cv.put(FieldsContrats.conso_prix,conso.prix)
        cv.put(FieldsContrats.conso_username,conso.username)
        cv.put(FieldsContrats.sync_status,sync_status)


        db.insert(FieldsContrats.TableConsommation, null, cv)
        MethodUtils.makeToastMessage(myContext,"Insertion locale de consommation reussie", ToastType.Long )
        db.close()
    }

    fun saveConsommationTolocalDatabase(quantite:String, date_cons:String, ref_client:String, type_conso:String, price:Int, username:String, sync_status: Int) {
        val db=this.writableDatabase
        val values = ContentValues()
        values.put(FieldsContrats.conso_qte,quantite)
        values.put(FieldsContrats.conso_date,date_cons)
        values.put(FieldsContrats.conso_ref_client,ref_client)
        values.put(FieldsContrats.conso_type,type_conso)
        values.put(FieldsContrats.conso_prix,price)
        values.put(FieldsContrats.conso_username, username)
        values.put(FieldsContrats.sync_status,sync_status)

        db.insert(FieldsContrats.TableConsommation, null, values)
        MethodUtils.makeToastMessage(myContext,"Insertion locale de consommation reussie", ToastType.Long )
        db.close()
    }

    fun readFuelFromLocalDatabase(database: SQLiteDatabase): Cursor {

        val projection = arrayOf<String>(FieldsContrats.conso_qte, FieldsContrats.conso_date, FieldsContrats.conso_ref_client,FieldsContrats.conso_type, FieldsContrats.SYNC_STATUS)

        return database.query(FieldsContrats.TableConsommation, projection, null, null, null, null, null)
    }



    /**
     * Allow to save model (QrCodeData object) into SQLite DataBase
     */
    fun insertQrCode(value_model: QrCodeData){
        val db = this@StormDatabaseHelper.writableDatabase
        val value = ContentValues()

        value.put(FieldsContrats.field_qrcode_text, value_model.qrcodeContent)
        value.put(FieldsContrats.field_date_saved, value_model.strDate)
        value.put(FieldsContrats.field_time_saved, value_model.strTime)

        db.insert(FieldsContrats.table_qrcode, null, value)
        db.close()
    }

    /**
     * Allow to get one record QrCode item by id passed has parameter, and the context
     */
    fun getQrCode(context:Context, idKey: Int): QrCodeData {
        val data = QrCodeData()
        val db = this@StormDatabaseHelper.writableDatabase
        val query = "select id,qrcode_text,date_saved,time_saved from ${FieldsContrats.table_qrcode} where id=$idKey"

        val cursor = db.rawQuery(query, null)

        if(cursor.count > 0){
            if (cursor.moveToNext()){
                setQrCodeValue(data, cursor)

                //Factory.makeToastMessage(context,"${cursor.count} recors found", ToastType.Long)
            }
        }else
           // Factory.makeToastMessage(context,"No recors found !!!", ToastType.Long)

        db.close()
        return data
    }

    /**
     * Factorisation function for multiple use
     */
    private fun setQrCodeValue(data: QrCodeData, cursor: Cursor) {
        data.intId = cursor.getInt(cursor.getColumnIndex(FieldsContrats.field_id))
        data.qrcodeContent = cursor.getString(cursor.getColumnIndex(FieldsContrats.field_qrcode_text))
        data.strDate = cursor.getString(cursor.getColumnIndex(FieldsContrats.field_date_saved))
        data.strTime = cursor.getString(cursor.getColumnIndex(FieldsContrats.field_time_saved))
    }


    val price: List<Price>
        get() {
            val taskList = ArrayList<Price>()
            val db = writableDatabase
            val selectQuery = "SELECT  * FROM ${FieldsContrats.TablePrice}"
            val cursor = db.rawQuery(selectQuery, null)
            if (cursor != null) {
                cursor.moveToFirst()
                while (cursor.moveToNext()) {
                    val tasks = Price()
                    tasks.id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(FieldsContrats.price_id)))
                    tasks.typeconso = cursor.getString(cursor.getColumnIndex(FieldsContrats.price_typeconso))
                    tasks.dateprix = cursor.getString(cursor.getColumnIndex(FieldsContrats.price_date))
                    tasks.prix = Integer.parseInt(cursor.getString(cursor.getColumnIndex(FieldsContrats.price_prix)))
                    taskList.add(tasks)
                }
            }
            cursor.close()
            return taskList
        }

    fun getOnePrice(_id: Int): Price {
        val tasks = Price()
        val db = writableDatabase
        val selectQuery = "SELECT  * FROM ${FieldsContrats.TablePrice} WHERE ${FieldsContrats.price_id} = $_id"
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor != null) {
            cursor.moveToFirst()
            while (cursor.moveToNext()) {
                tasks.id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(FieldsContrats.price_id)))
                tasks.typeconso = cursor.getString(cursor.getColumnIndex(FieldsContrats.price_typeconso))
                tasks.dateprix = cursor.getString(cursor.getColumnIndex(FieldsContrats.price_date))
                tasks.prix = Integer.parseInt(cursor.getString(cursor.getColumnIndex(FieldsContrats.price_prix)))
            }
        }
        cursor.close()
        return tasks
    }


    fun get_AllPrices(ctx:Context): java.util.ArrayList<Price> {
        val qry="select * from ${FieldsContrats.TablePrice}"
        val db=this.readableDatabase
        val cursor=db.rawQuery(qry,null)
        val prices= java.util.ArrayList<Price>()
        if(cursor.count==0)
            Toast.makeText(ctx,"Aucune donnee trouvee", Toast.LENGTH_LONG).show() else
        {
            while(cursor.moveToNext()){
                val tm=Price()
                tm.id=cursor.getInt(cursor.getColumnIndex(FieldsContrats.price_id))
                tm.typeconso=cursor.getString(cursor.getColumnIndex(FieldsContrats.price_typeconso))
                tm.dateprix=cursor.getString(cursor.getColumnIndex(FieldsContrats.price_date))
                tm.prix=cursor.getInt(cursor.getColumnIndex(FieldsContrats.price_prix))

                prices.add(tm)
            }
            Toast.makeText(ctx,"${cursor.count.toString()} enregistrements trouves", Toast.LENGTH_LONG).show()
        }
        cursor.close()
        db.close()
        return prices
    }


    fun get_AllConsoForSynchronisation(ctx:Context): java.util.ArrayList<Consommation> {
        val qry="select * from ${FieldsContrats.TableConsommation} where sync_status=0"
        val db=this.readableDatabase
        val cursor=db.rawQuery(qry,null)
        val consos= java.util.ArrayList<Consommation>()
        if(cursor.count==0)
            Toast.makeText(ctx,"Aucune donnee trouvee", Toast.LENGTH_LONG).show() else
        {
            while(cursor.moveToNext()){
                val tm=Consommation()
                tm.id=cursor.getInt(cursor.getColumnIndex(FieldsContrats.conso_id))
                tm.qte=cursor.getString(cursor.getColumnIndex(FieldsContrats.conso_qte))
                tm.date_cons=cursor.getString(cursor.getColumnIndex(FieldsContrats.conso_date))
                tm.type_conso=cursor.getString(cursor.getColumnIndex(FieldsContrats.conso_type))
                tm.ref_client=cursor.getString(cursor.getColumnIndex(FieldsContrats.conso_ref_client))
                tm.prix=cursor.getInt(cursor.getColumnIndex(FieldsContrats.conso_prix))
                tm.username=cursor.getString(cursor.getColumnIndex(FieldsContrats.conso_username))

                consos.add(tm)
            }
            Toast.makeText(ctx,"${cursor.count.toString()} enregistrements trouves", Toast.LENGTH_LONG).show()
        }
        cursor.close()
        db.close()
        return consos
    }


    fun go_InsertUnSyncedConsomma(ctx:Context) {
        val qry="select * from ${FieldsContrats.TableConsommation} where sync_status=0"
        val db=this.readableDatabase
        val cursor=db.rawQuery(qry,null)
       // val consos= java.util.ArrayList<Consommation>()
        if(cursor.count==0)
            Toast.makeText(ctx,"Aucune donnees a synchroniser", Toast.LENGTH_LONG).show() else
        {
            while(cursor.moveToNext()){
                val tm=Consommation()
                tm.id=cursor.getInt(cursor.getColumnIndex(FieldsContrats.conso_id))
                tm.qte=cursor.getString(cursor.getColumnIndex(FieldsContrats.conso_qte))
                tm.date_cons=cursor.getString(cursor.getColumnIndex(FieldsContrats.conso_date))
                tm.type_conso=cursor.getString(cursor.getColumnIndex(FieldsContrats.conso_type))
                tm.ref_client=cursor.getString(cursor.getColumnIndex(FieldsContrats.conso_ref_client))
                tm.prix=cursor.getInt(cursor.getColumnIndex(FieldsContrats.conso_prix))
                tm.username=cursor.getString(cursor.getColumnIndex(FieldsContrats.conso_username))

                /*
                Trying to sync with remote database
                 */
                MethodUtils.syncFuelToRemote(ctx,tm)
                //delete the current data
                deleteFromConsomma(tm.id)
                //consos.add(tm)
            }
            Toast.makeText(ctx,"${cursor.count.toString()} enregistrements trouves", Toast.LENGTH_LONG).show()
        }
        cursor.close()
        db.close()
        //return consos
    }

}