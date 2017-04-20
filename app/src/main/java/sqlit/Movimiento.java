package sqlit;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.UUID;

/**
 * Created by PMAT-PROGRAMADOR_1 on 10/04/2017.
 */

public class Movimiento {
    private long id;
    private String imagen;
    private String lectura;
    private String estado;

    public Movimiento( String imagen, String lectura, String estado) {
        this.imagen = imagen;
        this.lectura = lectura;
        this.estado = estado;
    }

    public Movimiento( Long id,String imagen, String lectura, String estado) {
        this.id =  id;
        this.imagen = imagen;
        this.lectura = lectura;
        this.estado = estado;
    }

    public Movimiento(Cursor cursor){
        id          =   Long.parseLong(cursor.getString(cursor.getColumnIndex(MovimientoContrac.MovimientoEntry.ID)));
        imagen      =   cursor.getString(cursor.getColumnIndex(MovimientoContrac.MovimientoEntry.IMAGEN));
        lectura   =   cursor.getString(cursor.getColumnIndex(MovimientoContrac.MovimientoEntry.IDMEDIDOR));
        estado      =   cursor.getString(cursor.getColumnIndex(MovimientoContrac.MovimientoEntry.ESTADO));
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(MovimientoContrac.MovimientoEntry.ID, id);
        values.put(MovimientoContrac.MovimientoEntry.IMAGEN, imagen);
        values.put(MovimientoContrac.MovimientoEntry.IDMEDIDOR, lectura);
        values.put(MovimientoContrac.MovimientoEntry.ESTADO, estado);
        return values;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImage() {
        return imagen;
    }

    public void setImage(String imagen) {
        this.imagen = imagen;
    }

    public String getIdmedidor() {
        return lectura;
    }

    public void setIdmedidor(String idmedidor) {
        this.lectura  = idmedidor;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
