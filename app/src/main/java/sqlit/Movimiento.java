package sqlit;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.UUID;

/**
 * Created by PMAT-PROGRAMADOR_1 on 10/04/2017.
 */

public class Movimiento {
    private long id;
    private String id_movimiento;
    private String imagen;
    private String lectura;
    private String estado;

    public Movimiento( String imagen, String lectura, String estado) {
        this.imagen = imagen;
        this.lectura = lectura;
        this.estado = estado;
    }

    public Movimiento( String id_movimiento,String imagen, String lectura, String estado) {
        this.id_movimiento =  id_movimiento;
        this.imagen = imagen;
        this.lectura = lectura;
        this.estado = estado;
    }



    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        //values.put(MovimientoContrac.MovimientoEntry.ID, id);
        values.put(MovimientoContrac.MovimientoEntry.ID_MOVIMIENTO,id_movimiento);
        values.put(MovimientoContrac.MovimientoEntry.IMAGEN, imagen);
        values.put(MovimientoContrac.MovimientoEntry.LECTURA, lectura);
        values.put(MovimientoContrac.MovimientoEntry.ESTADO, estado);
        return values;
    }

    public String getId_movimiento() {
        return id_movimiento;
    }

    public void setId_movimiento(String id_movimiento) {
        this.id_movimiento = id_movimiento;
    }

    public String getLectura() {
        return lectura;
    }

    public void setLectura(String lectura) {
        this.lectura = lectura;
    }

    public Long getId() {   return id;    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImage() {
        return imagen;
    }

    public void setImage(String imagen) {
        this.imagen = imagen;
    }



    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
