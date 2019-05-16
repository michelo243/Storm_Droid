package synapsehub.m.storm.helper

import synapsehub.m.storm.model.FragmentTagValue

/**
 * Created by Michelo on 2/8/19 at 12:42 AM.
 * for project -> Storm_droid
 * SynapseHub
 * michelo@synapse-hub.net
 */

class FieldsContrats {

    companion object {

        @JvmField var FRAGMENT_VALUE_TAG : FragmentTagValue = FragmentTagValue.Default
/*        @JvmField var FRAGMENT_VALUE_ID : Int = 0
        @JvmField val SYNC_STATUS_OK = 0
        @JvmField val SYNC_STAUTS_FAILED = 1*/
        const val SYNC_STATUS = "syncstatus"


        const val StormDD:String="Stormdroid.db"

        //details table
        const val TableUser:String="User"
        const val TableCustomer:String="tclient"
        const val TableConsommation:String="consommation"
        const val TablePrice:String="price"

        //details attributes
        private const val user_id:String="id"
        const val username:String="username"
        const val password:String="password"

        private const val customer_id="id"
        const val customer_matricule="matr_client"
        const val customer_name="nom_client"

        const val conso_id:String="id"
        const val conso_qte:String="qte"
        const val conso_date:String="date_cons"
        const val conso_type:String="type_conso"
        const val conso_ref_client="ref_client"
        const val conso_prix="prix"
        const val conso_username="username"
        const val sync_status:String="sync_status"


        const val price_id="id"
        const val price_typeconso="typeconso"
        const val price_prix="prix"
        const val price_date="dateprix"


        const val Create_Table_Price:String="CREATE TABLE " + TablePrice + " ( " +
                price_id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                price_typeconso + " VARCHAR (100), " +
                price_prix + " INTEGER, " +
                price_date + " VARCHAR ) "

        const val DropTable_Price:String= "DROP TABLE IF EXISTS $TablePrice"

        //Creation des tables
        const val Create_Table_User:String="CREATE TABLE " + TableUser + " ( " +
                user_id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                username + " VARCHAR (100), " +
                password + " VARCHAR ) "
        //SQL du drop de la table Customer
        const val DropTable_User:String="DROP TABLE IF EXISTS $TableUser"


        val Create_Table_Customer:String="CREATE TABLE $TableCustomer ( " +
                customer_id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                customer_matricule + " VARCHAR," +
                customer_name + " VARCHAR ) "

        const val DropTable_Customer:String= "DROP TABLE IF EXIST $TableCustomer"

        /*
        Concerne la table des consommations
         */
        const val Table_Conso_Create:String="CREATE TABLE $TableConsommation ( " +
                conso_id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                conso_qte + " VARCHAR, " +
                conso_ref_client + " VARCHAR, " +
                conso_type + " VARCHAR, " +
                conso_prix + " INTEGER, " +
                conso_date + " VARCHAR, " +
                conso_username + " VARCHAR, " +
                sync_status + " INTEGER )"

        const val DropTable_Conso:String= "DROP TABLE IF EXISTS $TableConsommation"

        /*
        This is for the Qrcode management
         */
        const val table_qrcode: String = "qrcode"

        //Here we declare all fiels to be use with that table
        const val field_id: String = "id"
        const val field_qrcode_text: String = "qrcode_text"
        const val field_date_saved: String = "date_saved"
        const val field_time_saved: String = "time_saved"

        //Script SQL for table qrcode
        const val create_table_qrcode = "create table $table_qrcode" +
                "(" +
                "$field_id integer primary key autoincrement," +
                "$field_qrcode_text varchar(100)," +
                "$field_date_saved varchar(10)," +
                "$field_time_saved varchar(8)" +
                ")"

        //Script drop table qrcode
        const val drop_table_qrcode = "drop table if exists $table_qrcode"

     }

}