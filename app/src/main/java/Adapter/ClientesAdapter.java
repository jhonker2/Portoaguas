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

import Models.Clientes;
import sqlit.Movimiento;

/**
 * Created by PMAT-PROGRAMADOR_1 on 19/07/2017.
 */

public class ClientesAdapter extends BaseAdapter {
    protected Activity activity;
    protected ArrayList<Clientes> items;
    public TextView cuenta,cedula,client,direcion;
    public Clientes item;


    public ClientesAdapter(Activity activity, ArrayList<Clientes> items) {
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
        return Integer.parseInt(items.get(position).getCuenta());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vi = inflater.inflate(R.layout.cliente_items_list, null);

        }

        item= items.get(position);
        cuenta = (TextView) vi.findViewById(R.id.l_cuenta);
        cedula = (TextView) vi.findViewById(R.id.l_cedula);
        client = (TextView) vi.findViewById(R.id.l_cliente);
        direcion = (TextView) vi.findViewById(R.id.l_direcion);

        cuenta.setText(item.getCuenta());
        cedula.setText(item.getCliente_());
        client.setText(item.getCliente_());
        direcion.setText(item.getDirecion());

        return vi;
    }
}
