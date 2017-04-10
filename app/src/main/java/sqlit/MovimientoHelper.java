package sqlit;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

//import static com.example.pmat_programador_1.portoaguas.sqlit.MovimientoContrac.MovimientoEntry;

/**
 * Created by PMAT-PROGRAMADOR_1 on 10/04/2017.
 */
public class MovimientoHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Portoaguas.db";

    public MovimientoHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + MovimientoContrac.MovimientoEntry.TABLE_NAME + " ("
                + MovimientoContrac.MovimientoEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + MovimientoContrac.MovimientoEntry.ID + " TEXT NOT NULL,"
                + MovimientoContrac.MovimientoEntry.IMAGEN + " TEXT NOT NULL,"
                + MovimientoContrac.MovimientoEntry.IDMEDIDOR + " TEXT NOT NULL,"
                + MovimientoContrac.MovimientoEntry.ESTADO + " TEXT NOT NULL,"
                + "UNIQUE (" + MovimientoContrac.MovimientoEntry.ID + "))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public long saveMovimiento(Movimiento movimiento) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        return sqLiteDatabase.insert(
                MovimientoContrac.MovimientoEntry.TABLE_NAME,
                null,
                movimiento.toContentValues());

    }
}
