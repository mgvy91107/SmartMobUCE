package ec.edu.uce.smartmobuce.controlador;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Henry on 1/17/2018.
 */

public class ControladorSQLite extends SQLiteOpenHelper {

    private static final String DB_NAME = "datosGPS.db";
    private static final int DB_VERSION = 1;
    //variable para almecenar

    private final String sqlCreate = "CREATE TABLE `DatosGPS` ( `dat_id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
            "`usu_id` INTEGER," +
            "`dat_latitud` REAL," +
            "`dat_longitud` REAL," +
            "`dat_precision` REAL," +
            "`dat_altitud` REAL," +
            "`dat_velocidad` REAL," +
            "`dat_proveedor` TEXT," +
            "`dat_fechahora_lectura` TEXT," +
            "`udpateStatus` TEXT" +
            ");";

    //contexto referencia al activity, name nombre de base de datos SQLiteDatabase factory no utilizamos ponemos null
    public ControladorSQLite(Context applicationcontext) {
        super(applicationcontext, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        //crea la dase de datos si no existe y ejecuta los comandos sql
        db.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query;
        query = "DROP TABLE IF EXISTS DatosGPS";
        //For now, clear the database and re-create
        db.execSQL(query);
        onCreate(db);
    }

    /**
     * Inserts datos into SQLite DB
     *
     * @param queryValues
     */
    public void insertDatos(HashMap<String, String> queryValues) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("dat_id", queryValues.get("dat_id"));
        values.put("usu_id", queryValues.get("usu_id"));
        values.put("dat_latitud", queryValues.get("dat_latitud"));
        values.put("dat_longitud", queryValues.get("dat_longitud"));
        values.put("dat_precision", queryValues.get("dat_precision"));
        values.put("dat_altitud", queryValues.get("dat_altitud"));
        values.put("dat_velocidad", queryValues.get("dat_velocidad"));
        values.put("dat_proveedor", queryValues.get("dat_proveedor"));
        values.put("dat_fechahora_lectura", queryValues.get("dat_fechahora_lectura"));
        values.put("udpateStatus", "no");
        database.insert("DatosGPS", null, values);
        database.close();
    }

    /**
     * Get list of datos from SQLite DB as Array List
     *
     * @return
     */
    public ArrayList<HashMap<String, String>> getAllData() {
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  * FROM DatosGPS";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("dat_id", cursor.getString(0));
                map.put("usu_id", cursor.getString(1));
                map.put("dat_latitud", cursor.getString(2));
                map.put("dat_longitud", cursor.getString(3));
                map.put("dat_precision", cursor.getString(4));
                map.put("dat_altitud", cursor.getString(5));
                map.put("dat_velocidad", cursor.getString(6));
                map.put("dat_proveedor", cursor.getString(7));
                map.put("dat_fechahora_lectura", cursor.getString(8));
                map.put("udpateStatus", cursor.getString(9));
                wordList.add(map);
            } while (cursor.moveToNext());
        }
        database.close();
        return wordList;
    }


    /**
     * Compose JSON out of SQLite records
     *
     * @return
     */
    public String composeJSONfromSQLite() {
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  * FROM DatosGPS where udpateStatus = '" + "no" + "'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("dat_id", cursor.getString(0));
                map.put("usu_id", cursor.getString(1));
                map.put("dat_latitud", cursor.getString(2));
                map.put("dat_longitud", cursor.getString(3));
                map.put("dat_precision", cursor.getString(4));
                map.put("dat_altitud", cursor.getString(5));
                map.put("dat_velocidad", cursor.getString(6));
                map.put("dat_proveedor", cursor.getString(7));
                map.put("dat_fechahora_lectura", cursor.getString(8));
                map.put("udpateStatus", cursor.getString(9));
                wordList.add(map);
            } while (cursor.moveToNext());
        }
        database.close();
        Gson gson = new GsonBuilder().create();
        //Use GSON to serialize Array List to JSON
        return gson.toJson(wordList);
    }


    /**
     * Get SQLite records that are yet to be Synced
     *
     * @return
     */
    public int dbSyncCount() {

        int count = 0;
        String selectQuery = "SELECT  count (*) FROM DatosGPS where udpateStatus = '" + "no" + "'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        count = cursor.getInt(0);
        cursor.close();
        database.close();
        return count;
    }

    /**
     * Update Sync status against each User ID
     *
     * @param usu_id
     * @param dat_fechahora_lectura
     * @param status
     */
    public void updateSyncStatus(String usu_id, String dat_fechahora_lectura, String status) {
        SQLiteDatabase database = this.getWritableDatabase();
        String updateQuery = "Update DatosGPS set udpateStatus = '" + status + "' where usu_id='" + usu_id + "'" + " and dat_fechahora_lectura='" + dat_fechahora_lectura + "'";

        database.execSQL(updateQuery);
        database.close();
    }

    public void eraseSync() {
        SQLiteDatabase database = this.getWritableDatabase();
        String updateQuery = "DELETE FROM DatosGPS WHERE udpateStatus = 'yes'";

        database.execSQL(updateQuery);
        database.close();
    }

}
