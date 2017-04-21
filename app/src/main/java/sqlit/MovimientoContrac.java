package sqlit;

import android.provider.BaseColumns;

/**
 * Created by PMAT-PROGRAMADOR_1 on 10/04/2017.
 */

public class MovimientoContrac {

    public static abstract class MovimientoEntry implements BaseColumns{
        public static final String TABLE_NAME="movimientos";
        public static final String ID = "id";
        public static final String ID_MOVIMIENTO = "id_movimiento";
        public static final String IMAGEN = "imagen";
        public static final String LECTURA = "lectura";
        public static final String ESTADO = "estado";
    }
}
