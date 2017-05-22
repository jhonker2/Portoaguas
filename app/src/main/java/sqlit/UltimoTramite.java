package sqlit;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by PMAT-PROGRAMADOR_1 on 22/05/2017.
 */

public class UltimoTramite extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Portobd.db";


    public UltimoTramite(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static class Datos_Max_tramites implements BaseColumns {
        public static final String TABLA="Ultimo_tramites";
        public static final String ID="id";
        public static final String USUARIO_OFICIAL="usuario_oficial";
        public static final String ID_TRAMITE="id_tramite";

        private static final String TEXT_TYPE=" TEXT";
        private static final String INTEGER_TYPE=" INTEGER";
        private static final String REAL_TYPE=" REAL";

        private static final String COMMA_SEP=",";

        private static final String CREAR_TABLA_MAXTRAMITES=
                "CREATE TABLE "+ Datos_Max_tramites.TABLA +" ("+
                        Datos_Max_tramites.ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                        Datos_Max_tramites.USUARIO_OFICIAL+TEXT_TYPE+
                        Datos_Max_tramites.ID_TRAMITE+INTEGER_TYPE+COMMA_SEP+" )";

        private static final String SQL_ELIMINAR_TABLA=
                "DROP TABLE IF EXISTS" + Datos_Max_tramites.TABLA;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Datos_Max_tramites.CREAR_TABLA_MAXTRAMITES);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
