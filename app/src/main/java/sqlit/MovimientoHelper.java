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
            "(id INTEGER PRIMARY KEY AUTOINCREMENT, id_movimiento TEXT, imagen TEXT, lectura TEXT, estado TEXT)";

    public MovimientoHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLA_MOVIMIENTOS);
    }

    public ArrayList<Movimiento> recuperarCONTACTOS() {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Movimiento> lista_contactos = new ArrayList<Movimiento>();
        String[] valores_recuperar = {"id_movimiento", "imagen", "lectura", "estado"};
        Cursor c = db.query("movimientos", valores_recuperar,
                null, null, null, null, null, null);
        c.moveToFirst();
        do {
            lista_contactos.add(new Movimiento(c.getString(0), c.getString(1),
                    c.getString(2), c.getString(3)));
        } while (c.moveToNext());
        db.close();
        c.close();
        return lista_contactos;
    }

    public int TotalMovimientos() {
        SQLiteDatabase db = getReadableDatabase();
        int cont = 0;
        String[] valores_recuperar = {"id_movimiento", "imagen", "lectura", "estado"};
        Cursor c = db.query("movimientos", valores_recuperar,
                null, null, null, null, null, null);
        c.moveToFirst();
        do {
            cont++;
        } while (c.moveToNext());
        db.close();
        c.close();
        return cont;
    }


    public long saveMovimiento(Movimiento movimiento) {

        return getWritableDatabase().insert(
                MovimientoContrac.MovimientoEntry.TABLE_NAME,
                null,
                movimiento.toContentValues());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
