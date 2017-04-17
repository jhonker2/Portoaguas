package Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pmat_programador_1.portoaguas.MapsActivity;
import com.example.pmat_programador_1.portoaguas.R;

import java.util.ArrayList;

import Models.Detalles;
import Models.Rubros;

import static com.example.pmat_programador_1.portoaguas.MapsActivity.total;

/**
 * Created by PMAT-PROGRAMADOR_1 on 13/04/2017.
 */

    public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ItemRowHolder> {
    private ArrayList<Rubros> items;
    public ArrayList<Detalles> detall= new ArrayList<Detalles>();
    private Context mContext;
    public double contTotal=0;
    public class ItemRowHolder extends RecyclerView.ViewHolder {

        protected TextView titulo, precio,codigo;
        public Spinner cantidad_s;
        protected RecyclerView recycler_view_list;


        public ItemRowHolder(View view) {
            super(view);
            codigo = (TextView) view.findViewById(R.id.codigo);
            titulo = (TextView) view.findViewById(R.id.card_titulo);
            precio = (TextView) view.findViewById(R.id.card_precio_val);
            recycler_view_list = (RecyclerView) view.findViewById(R.id.my_recycler_view);
            cantidad_s  =   (Spinner) view.findViewById(R.id.cantidad_spi);
            String []cantidades={"0","1","2","3","4","5","6","7","8","9","10"};
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, cantidades);
            cantidad_s.setAdapter(adapter);


            cantidad_s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Log.e("Items del detalle", String.valueOf(items.size()));
                    if(detall.isEmpty()) {
                        detall.add(new Detalles(cantidad_s.getSelectedItem().toString(), codigo.getText().toString(), precio.getText().toString()));
                    }else{
                        int aux=0;
                        for (int x=0; x<detall.size(); x++){
                            if(detall.get(x).getCodigo().equals(codigo.getText())){
                                detall.set(x,new Detalles(cantidad_s.getSelectedItem().toString(), codigo.getText().toString(), precio.getText().toString()));
                                aux++;
                            }
                        }
                        if(aux==0) {
                            detall.add(new Detalles(cantidad_s.getSelectedItem().toString(), codigo.getText().toString(), precio.getText().toString()));
                        }
                    }
                    contTotal=0;
                    for (int y=0; y<detall.size(); y++){

                        contTotal=contTotal+(Double.parseDouble(detall.get(y).getCantidad())*Double.parseDouble(detall.get(y).getPrecio()));
                        total.setText(String.valueOf(contTotal));
                    }

                        Log.e("CANTIDADES DETALLES:", String.valueOf( detall.size()));



                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
/*
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    Toast.makeText(v.getContext(), precio.getText(),Toast.LENGTH_SHORT).show();

                    AlertDialog.Builder buil = new AlertDialog.Builder(v.getContext());
                    buil.setTitle(titulo.getText());
                    final NumberPicker numberPicker = new NumberPicker(v.getContext());
                    numberPicker.setMaxValue(20);
                    numberPicker.setMinValue(1);
                    numberPicker.setWrapSelectorWheel(false);
                    buil.setView(numberPicker);
                    buil.setPositiveButton("Aceptar", new DialogInterface.OnClickListener(){

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            contTotal = contTotal+(Double.parseDouble(precio.getText().toString()) * numberPicker.getValue());

                            Toast.makeText(v.getContext(),"Total:"+contTotal,Toast.LENGTH_SHORT).show();
                            total.setText(String.valueOf(contTotal));
                        }
                    });
                    buil.show();

                }
            });*/

        }
    }



    public RecycleViewAdapter(Context context,ArrayList<Rubros> rubros  ){
        this.items=rubros;
        this.mContext = context;
    }


    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_details_cards, parent,false);
        ItemRowHolder mh = new ItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(final ItemRowHolder holder, int position) {
        holder.codigo.setText(items.get(position).getCodigo());
        holder.titulo.setText(items.get(position).getTitulos());
        holder.precio.setText(items.get(position).getPrecio());





    }

    @Override
    public int getItemCount() {
        return items.size();
    }



}
