package sqlit;

import android.provider.BaseColumns;

/**
 * Created by PMAT-PROGRAMADOR_1 on 12/05/2017.
 */

public  class TramiteContrac{
    public static abstract class TramitesEntry implements BaseColumns {

        public static final String TABLE_NAME = "tramites";
        public static final String ID = "id";
        public static final String ID_TRAMITE = "id_tramite";
        public static final String ID_TAREA_TRAMITE = "id_tarea_tramite";
        public static final String NUMERO_CUENTA = "numero_cuenta";
        public static final String LATITUD = "latitud";
        public static final String LONGITUD = "longitud";
        public static final String COD_CLIENTE = "cod_cliente";
        public static final String COD_PREDIO = "cod_predio";
        public static final String DEUDA_PORTOAGUAS = "deuda_portoaguas";
        public static final String MES_DEUDA = "mes_deuda";
        public static final String COD_MEDIDOR = "cod_medidor";
        public static final String SERIE_MEDIDOR = "serie_medidor";
    }
}
