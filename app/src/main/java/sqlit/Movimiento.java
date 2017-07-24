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
    private String lat_reg_trab;
    private String long_reg_trab;
    private String sal_abil;
    private String total_mov;
    private String id_tarea_tramite;
    private String tabla;
    private String observacion;
    private boolean seleccionado=false;

    public boolean isSeleccionado() {
        return seleccionado;
    }

    public void setSeleccionado(boolean seleccionado) {
        this.seleccionado = seleccionado;
    }

    public Movimiento(long id, String imagen, String lat_reg_trab, String long_reg_trab, String sal_abil, String total_mov, String tabla, String observacion, String id_tarea_tramite) {
        this.id = id;
        this.imagen = imagen;
        this.lat_reg_trab = lat_reg_trab;
        this.long_reg_trab = long_reg_trab;
        this.sal_abil = sal_abil;
        this.total_mov = total_mov;
        this.tabla = tabla;
        this.observacion = observacion;
        this.id_tarea_tramite =  id_tarea_tramite;
    }

    public String getId_tarea_tramite() {
        return id_tarea_tramite;
    }

    public void setId_tarea_tramite(String id_tarea_tramite) {
        this.id_tarea_tramite = id_tarea_tramite;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getLat_reg_trab() {
        return lat_reg_trab;
    }

    public void setLat_reg_trab(String lat_reg_trab) {
        this.lat_reg_trab = lat_reg_trab;
    }

    public String getLong_reg_trab() {
        return long_reg_trab;
    }

    public void setLong_reg_trab(String long_reg_trab) {
        this.long_reg_trab = long_reg_trab;
    }

    public String getSal_abil() {
        return sal_abil;
    }

    public void setSal_abil(String sal_abil) {
        this.sal_abil = sal_abil;
    }

    public String getTotal_mov() {
        return total_mov;
    }

    public void setTotal_mov(String total_mov) {
        this.total_mov = total_mov;
    }

    public String getTabla() {
        return tabla;
    }

    public void setTabla(String tabla) {
        this.tabla = tabla;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }


}




