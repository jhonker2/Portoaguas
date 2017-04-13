package Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.pmat_programador_1.portoaguas.R;

import java.util.ArrayList;

import sqlit.Movimiento;

/**
 * Created by PMAT-PROGRAMADOR_1 on 12/04/2017.
 */

public class MovimientsAdapter2 extends BaseAdapter {
    protected Activity activity;
    protected ArrayList<Movimiento> items;
    public TextView codigo,imagen,idmedidor,estado;
    public Movimiento item;

    public MovimientsAdapter2(Activity activity, ArrayList<Movimiento> items) {
        this.activity = activity;
        this.items = items;
    }


    @Override
    public int getCount() {

            return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return items.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vi = inflater.inflate(R.layout.bd_items, null);

        }else{

        }

        item= items.get(position);
        codigo         = (TextView) vi.findViewById(R.id.t_id);
        idmedidor   = (TextView) vi.findViewById(R.id.t_idmedidor);
        estado      = (TextView) vi.findViewById(R.id.t_estado);

        codigo.setText(String.valueOf(item.getId()));
        idmedidor.setText(item.getIdmedidor());
        estado.setText(item.getEstado());

        return vi;
    }
}
