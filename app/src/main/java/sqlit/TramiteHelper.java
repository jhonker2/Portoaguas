package sqlit;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by PMAT-PROGRAMADOR_1 on 12/05/2017.
 */

public class TramiteHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Portobd.db";

    private static final String TABLA_TRAMITES = "CREATE TABLE tramites" +
            "(id INTEGER PRIMARY KEY AUTOINCREMENT, id_tramite TEXT, id_tarea_tramite TEXT, munero_cuenta TEXT, latitud TEXT, longitud TEXT,  cod_cliente TEXT, cod_predio TEXT, deuda_portoaguas TEXT, mes_deuda TEXT, cod_medidor TEXT, serie_medidor TEXT)";

    public TramiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLA_TRAMITES);
    }

    public ArrayList<Tramites> recuperarTRAMITES() {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Tramites> lista_tramites = new ArrayList<Tramites>();
        String[] valores_recuperar = {"id_tramite", "id_tarea_tramite", "numero_cuenta", "latitud","longitud","cod_cliente","cod_predio","deud_portoaguas","mes_deuda","cod_medidor","serie_medidor"};
        Cursor c = db.query("tramites", valores_recuperar,
                null, null, null, null, null, null);
        c.moveToFirst();
        do {
            lista_tramites.add(new Tramites(c.getLong(0), c.getLong(1),
                    c.getLong(2), c.getLong(3),c.getLong(4),c.getLong(5),c.getDouble(6),
                    c.getDouble(7),c.getFloat(8),c.getString(9),c.getString(10)));
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


    public long saveTramites(Tramites tramites) {

        return getWritableDatabase().insert(
                TramiteContrac.TramitesEntry.TABLE_NAME,
                null,
                tramites.toContentValues());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
