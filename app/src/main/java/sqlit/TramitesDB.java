package sqlit;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * Created by PMAT-PROGRAMADOR_1 on 22/05/2017.
 */

public class TramitesDB extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Portobd.db";

    public TramitesDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static class Datos_tramites implements BaseColumns{
        public static final String TABLA="tramites";
        public static final String ID="id";
        public static final String ID_TRAMITE="id_tramite";
        public static final String ID_TAREA_TRAMITE="id_tarea_tramite";
        public static final String NUMERO_CUENTA="numero_cuenta";
        public static final String COD_CLIENTE="cod_cliente";
        public static final String COD_PREDIO="cod_predio";
        public static final String MES_DEUDA="mes_deuda";
        public static final String LATITUD="latitud";
        public static final String LONGITUD="longitud";
        public static final String DEUDA_PORTOAGUAS="deuda_portoaguas";
        public static final String COD_MEDIDOR="cod_medidor";
        public static final String SERIE_MEDIDOR="serie_medidor";
        public static final String ESTADO_TRAMITE="estado_tramite";


        private static final String TEXT_TYPE=" TEXT";
        private static final String INTEGER_TYPE=" INTEGER";
        private static final String REAL_TYPE=" REAL";

        private static final String COMMA_SEP=",";

        private static final String CREAR_TABLA_TRAMITES=
                    "CREATE TABLE "+ Datos_tramites.TABLA +" ("+
                            Datos_tramites.ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                            Datos_tramites.ID_TRAMITE+INTEGER_TYPE+COMMA_SEP+
                            Datos_tramites.ID_TAREA_TRAMITE+INTEGER_TYPE+COMMA_SEP+
                            Datos_tramites.NUMERO_CUENTA+INTEGER_TYPE+COMMA_SEP+
                            Datos_tramites.COD_CLIENTE+INTEGER_TYPE+COMMA_SEP+
                            Datos_tramites.COD_PREDIO+INTEGER_TYPE+COMMA_SEP+
                            Datos_tramites.MES_DEUDA+INTEGER_TYPE+COMMA_SEP+
                            Datos_tramites.LATITUD+TEXT_TYPE+COMMA_SEP+
                            Datos_tramites.LONGITUD+TEXT_TYPE+COMMA_SEP+
                            Datos_tramites.DEUDA_PORTOAGUAS+TEXT_TYPE+COMMA_SEP+
                            Datos_tramites.COD_MEDIDOR+TEXT_TYPE+COMMA_SEP+
                            Datos_tramites.SERIE_MEDIDOR+TEXT_TYPE+COMMA_SEP+
                            Datos_tramites.ESTADO_TRAMITE+TEXT_TYPE+" )";

        private static final String SQL_ELIMINAR_TABLA=
                "DROP TABLE IF EXISTS" + Datos_tramites.TABLA;



    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Datos_tramites.CREAR_TABLA_TRAMITES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
       // db.execSQL(Datos_tramites.SQL_ELIMINAR_TABLA);
    }
}
