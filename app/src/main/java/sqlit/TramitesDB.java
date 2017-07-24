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
        public static final String TABLA_MOVIMIENTOS = "movimientos";
        public static final String TABLA_MAXTRAMITE="Ultimo_tramites";
        public static final String TABLA_TRAB_MOV="trab_mov";
        public static final String TABLA_GEOLOCALIZACION="geolocalizacion";


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
        public static final String TIPO_TRAMITE="tipo_tramite";
        public static final String CLIENTE="cliente";
        public static final String ESTADO_MEDIDOR="estado_medidor";


        public static final String IMAGEN = "imagen";
        public static final String OBSERVACION = "observacion";
        public static final String ESTADO = "estado";

        public static final String USUARIO_OFICIAL="usuario_oficial";

        public static final String LAT_REG_TRAB = "lat_reg_trab";
        public static final String LONG_REG_TRAB = "long_reg_trab";
        public static final String SAL_ABIL = "sal_abil";
        public static final String TOTAL_MOV = "total_mov";
        public static final String TABLA_INFO = "tabla";

        public static final String ID_DISPOSITIVO = "id_dispositivo";
        public static final String ID_DISPOSITIVO_USUARIO = "id_dispositivo_usuario";
        public static final String FECHA = "fecha";
        public static final String HORA = "hora";



        private static final String TEXT_TYPE=" TEXT";
        private static final String INTEGER_TYPE=" INTEGER";
        private static final String REAL_TYPE=" REAL";

        private static final String COMMA_SEP=",";

        private static final String CREAR_TABLA_TRAB_MOV=
                 "CREATE TABLE "+ Datos_tramites.TABLA_TRAB_MOV+"("+
                         Datos_tramites.ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                         Datos_tramites.LAT_REG_TRAB+TEXT_TYPE+COMMA_SEP+
                         Datos_tramites.LONG_REG_TRAB+TEXT_TYPE+COMMA_SEP+
                         Datos_tramites.ID_TAREA_TRAMITE+TEXT_TYPE+COMMA_SEP+
                         Datos_tramites.OBSERVACION+TEXT_TYPE+COMMA_SEP+
                         Datos_tramites.SAL_ABIL+TEXT_TYPE+COMMA_SEP+
                         Datos_tramites.TOTAL_MOV+TEXT_TYPE+COMMA_SEP+
                         Datos_tramites.IMAGEN+TEXT_TYPE+COMMA_SEP+
                         Datos_tramites.TABLA_INFO+TEXT_TYPE+" )";


        private static final String CREAR_TABLA_MAXTRAMITES=
                "CREATE TABLE "+ Datos_tramites.TABLA_MAXTRAMITE +" ("+
                        Datos_tramites.ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                        Datos_tramites.USUARIO_OFICIAL+TEXT_TYPE+COMMA_SEP+
                        Datos_tramites.ID_TRAMITE+INTEGER_TYPE+" )";

        private static final String CREAR_TABLA_GEOLOCALIZACION=
                "CREATE TABLE "+ Datos_tramites.TABLA_GEOLOCALIZACION +" ("+
                        Datos_tramites.ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                        Datos_tramites.ID_DISPOSITIVO+TEXT_TYPE+COMMA_SEP+
                        Datos_tramites.ID_DISPOSITIVO_USUARIO+TEXT_TYPE+COMMA_SEP+
                        Datos_tramites.LATITUD+TEXT_TYPE+COMMA_SEP+
                        Datos_tramites.LONGITUD+TEXT_TYPE+COMMA_SEP+
                        Datos_tramites.FECHA+TEXT_TYPE+COMMA_SEP+
                        Datos_tramites.HORA+TEXT_TYPE+" )";

        private static final String SQL_ELIMINAR_TABLA_MAXTRAMITE=
                "DROP TABLE IF EXISTS" + Datos_tramites.TABLA;
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
                            Datos_tramites.ESTADO_TRAMITE+TEXT_TYPE+COMMA_SEP+
                            Datos_tramites.TIPO_TRAMITE+TEXT_TYPE+COMMA_SEP+
                            Datos_tramites.CLIENTE+TEXT_TYPE+COMMA_SEP+
                            Datos_tramites.ESTADO_MEDIDOR+TEXT_TYPE+COMMA_SEP+
                            Datos_tramites.USUARIO_OFICIAL+TEXT_TYPE+" )";
        private static final String CREAR_TABLA_MOVIMIENTO=
                "CREATE TABLE "+ Datos_tramites.TABLA_MOVIMIENTOS +" ("+
                        Datos_tramites.ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                        Datos_tramites.ID_TAREA_TRAMITE+INTEGER_TYPE+COMMA_SEP+
                        Datos_tramites.IMAGEN+TEXT_TYPE+COMMA_SEP+
                        Datos_tramites.OBSERVACION+TEXT_TYPE+COMMA_SEP+
                        Datos_tramites.ESTADO+TEXT_TYPE+ ")";

        private static final String SQL_ELIMINAR_TABLA_TRAMITES=
                "DROP TABLE IF EXISTS" + Datos_tramites.TABLA;
        private static final String SQL_ELIMINAR_TABLA_MOVIMIENTOS=
                "DROP TABLE IF EXISTS" + Datos_tramites.TABLA_MOVIMIENTOS;



    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Datos_tramites.CREAR_TABLA_TRAMITES);
        db.execSQL(Datos_tramites.CREAR_TABLA_MOVIMIENTO);
        db.execSQL(Datos_tramites.CREAR_TABLA_MAXTRAMITES);
        db.execSQL(Datos_tramites.CREAR_TABLA_TRAB_MOV);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(Datos_tramites.SQL_ELIMINAR_TABLA_MOVIMIENTOS);
        db.execSQL(Datos_tramites.SQL_ELIMINAR_TABLA_TRAMITES);
        db.execSQL(Datos_tramites.SQL_ELIMINAR_TABLA_MAXTRAMITE);
    }
}
