package sqlit;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.UUID;

/**
 * Created by PMAT-PROGRAMADOR_1 on 10/04/2017.
 */

public class Movimiento {
    private String id;
    private String imagen;
    private String idmedidor;
    private String estado;

    public Movimiento( String imagen, String idmedidor, String estado) {
        this.id =  UUID.randomUUID().toString();
        this.imagen = imagen;
        this.idmedidor = idmedidor;
        this.estado = estado;
    }

    public Movimiento( String id,String imagen, String idmedidor, String estado) {
        this.id =  id;
        this.imagen = imagen;
        this.idmedidor = idmedidor;
        this.estado = estado;
    }

    public Movimiento(Cursor cursor){
        id          =   cursor.getString(cursor.getColumnIndex(MovimientoContrac.MovimientoEntry.ID));
        imagen      =   cursor.getString(cursor.getColumnIndex(MovimientoContrac.MovimientoEntry.IMAGEN));
        idmedidor   =   cursor.getString(cursor.getColumnIndex(MovimientoContrac.MovimientoEntry.IDMEDIDOR));
        estado      =   cursor.getString(cursor.getColumnIndex(MovimientoContrac.MovimientoEntry.ESTADO));
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(MovimientoContrac.MovimientoEntry.ID, id);
        values.put(MovimientoContrac.MovimientoEntry.IMAGEN, imagen);
        values.put(MovimientoContrac.MovimientoEntry.IDMEDIDOR, idmedidor);
        values.put(MovimientoContrac.MovimientoEntry.ESTADO, estado);
        return values;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return imagen;
    }

    public void setImage(String imagen) {
        this.imagen = imagen;
    }

    public String getIdmedidor() {
        return idmedidor;
    }

    public void setIdmedidor(String idmedidor) {
        this.idmedidor = idmedidor;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
