package sqlit;

import android.content.ContentValues;

import java.util.UUID;

/**
 * Created by PMAT-PROGRAMADOR_1 on 10/04/2017.
 */

public class Movimiento {
    public String id;
    public String imagen;
    public String idmedidor;
    public String estado;

    public Movimiento( String imagen, String idmedidor, String estado) {
        this.id = UUID.randomUUID().toString();
        this.imagen = imagen;
        this.idmedidor = idmedidor;
        this.estado = estado;
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
