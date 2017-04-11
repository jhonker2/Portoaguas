package Adapter;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.pmat_programador_1.portoaguas.R;

import java.util.ArrayList;

import sqlit.Movimiento;
import sqlit.MovimientoContrac;

/**
 * Created by PMAT-PROGRAMADOR_1 on 10/04/2017.
 */

public class MovimientosAdapter extends CursorAdapter {
    protected ArrayList<Movimiento> items;
    public TextView _id,idmedidor,estado;
    public Movimiento item;

    public MovimientosAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(R.layout.bd_items, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        _id         = (TextView) view.findViewById(R.id.t_id);
        idmedidor   = (TextView) view.findViewById(R.id.t_idmedidor);
        estado      = (TextView) view.findViewById(R.id.t_estado);


        _id.setText(cursor.getString(cursor.getColumnIndex(MovimientoContrac.MovimientoEntry._ID)));
        idmedidor.setText(cursor.getString(cursor.getColumnIndex(MovimientoContrac.MovimientoEntry.IDMEDIDOR)));
        estado.setText(cursor.getString(cursor.getColumnIndex(MovimientoContrac.MovimientoEntry.ESTADO)));
    }


}
