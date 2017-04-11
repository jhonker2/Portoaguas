package sqlit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//import static com.example.pmat_programador_1.portoaguas.sqlit.MovimientoContrac.MovimientoEntry;

/**
 * Created by PMAT-PROGRAMADOR_1 on 10/04/2017.
 */
public class MovimientoHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Portobd.db";

    private static final String TABLA_MOVIMIENTOS = "CREATE TABLE movimientos" +
            "(_id INTEGER PRIMARY KEY AUTOINCREMENT,id TEXT, imagen TEXT, idmedidor TEXT, estado TEXT)";
    public MovimientoHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLA_MOVIMIENTOS);

        //mockData(db);
    }

    public List<Movimiento> recuperarCONTACTOS() {
        SQLiteDatabase db = getReadableDatabase();
        List<Movimiento> lista_contactos = new ArrayList<Movimiento>();
        String[] valores_recuperar = {"id", "imagen", "idmedidor", "estado"};
        Cursor c = db.query("movimientos", valores_recuperar,
                null, null, null, null, null, null);
        c.moveToFirst();
        do {
            //Movimiento contactos = new Movimiento(c.getString(0), c.getString(1),
                   // c.getString(2));
            lista_contactos.add(new Movimiento(c.getString(0), c.getString(1),
                    c.getString(2),c.getString(3)));
        } while (c.moveToNext());
        db.close();
        c.close();
        return lista_contactos;
    }

    private void mockData(SQLiteDatabase sqLiteDatabase) {
        mockLawyer(sqLiteDatabase, new Movimiento("Ruta de la imagen", "012","A"));
        mockLawyer(sqLiteDatabase, new Movimiento("Ruta de la imagen2", "0142","A"));

    }

    public boolean insertarMovimiento(String imagen, String idmedidor, String estado ){
        boolean res=false;
        SQLiteDatabase db = getWritableDatabase();
        String id_= UUID.randomUUID().toString();
        if(db != null){
            ContentValues valores = new ContentValues();
            valores.put("id",id_);
            valores.put("imagen", imagen);
            valores.put("idmedidor", idmedidor);
            valores.put("estado", estado);
            db.insert("movimientos", null, valores);
            db.close();
            res=true;
        }
        return res;
    }

    public long mockLawyer(SQLiteDatabase db, Movimiento movimiento) {
        return db.insert(
                MovimientoContrac.MovimientoEntry.TABLE_NAME,
                null,
                movimiento.toContentValues());
    }
    public long saveMovimiento( Movimiento movimiento) {

       return getWritableDatabase().insert(
                MovimientoContrac.MovimientoEntry.TABLE_NAME,
                null,
                movimiento.toContentValues());
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /*db.execSQL("DROP TABLE IF EXISTS " + TABLA_MOVIMIENTOS);
        onCreate(db);*/
    }



    public Cursor getAllMovimientos() {
        return getReadableDatabase()
                .query(
                        MovimientoContrac.MovimientoEntry.TABLE_NAME,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null);
    }
}
