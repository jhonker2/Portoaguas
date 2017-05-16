package sqlit;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import Models.Puntos;

/**
 * Created by PMAT-PROGRAMADOR_1 on 12/05/2017.
 */

public class TramiteHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Portobd.db";

    private static final String TABLA_TRAMITES = "CREATE TABLE tramites" +
            "(id INTEGER PRIMARY KEY AUTOINCREMENT, id_tramite INTEGER, id_tarea_tramite INTEGER, munero_cuenta INTEGER, latitud REAL, longitud REAL,  cod_cliente INTEGER, cod_predio INTEGER, deuda_portoaguas REAL, mes_deuda INTEGER, cod_medidor INTEGER, serie_medidor TEXT)";

    public TramiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLA_TRAMITES);
    }

    public ArrayList<Puntos> recuperarTRAMITES() {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Puntos> lista_tramites = new ArrayList<Puntos>();
        String[] valores_recuperar = {"serie_medidor","latitud","longitud","deuda_portoaguas","id_tramite", "numero_cuenta", "cod_cliente","mes_deuda","cod_medidor","cod_predio","id_tarea_tramite"};
        Cursor c = db.query("tramites", valores_recuperar,
                null, null, null, null, null, null);
        c.moveToFirst();
        do {
            lista_tramites.add(new Puntos(c.getString(0), c.getDouble(1),
                    c.getDouble(2), c.getDouble(3),c.getLong(4),c.getLong(5),c.getLong(6),
                    c.getLong(7),c.getLong(8),c.getLong(9),c.getLong(10)));
        } while (c.moveToNext());
        db.close();
        c.close();
        return lista_tramites;
    }

    public int TotalTramites() {
        SQLiteDatabase db = getReadableDatabase();
        int cont = 0;
        String[] valores_recuperar = {"id_tramite"};
        Cursor c = db.query("tramites", valores_recuperar,
                null, null, null, null, null, null);
        c.moveToFirst();

        if(c.getCount()==0){

        }else {
            do {
                cont++;
            } while (c.moveToNext());
        }
        db.close();
        c.close();
        return cont;
    }


    public long saveTramites(Puntos tramites) {

        return getWritableDatabase().insert(
                TramiteContrac.TramitesEntry.TABLE_NAME,
                null,
                tramites.toContentValues());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
