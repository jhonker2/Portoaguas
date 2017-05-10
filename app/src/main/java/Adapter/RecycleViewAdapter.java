package Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

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
    String []cantidades={"0","1","2","3","4","5","6","7","8","9","10"};
    public class ItemRowHolder extends RecyclerView.ViewHolder {

        protected TextView titulo, precio,codigo, cod_prod;
        public Spinner cantidad_s;
        protected RecyclerView recycler_view_list;


        public ItemRowHolder(View view) {
            super(view);
            codigo  = (TextView) view.findViewById(R.id.codigo);
            titulo  = (TextView) view.findViewById(R.id.card_titulo);
            precio  = (TextView) view.findViewById(R.id.card_precio_val);
            cod_prod= (TextView) view.findViewById(R.id.txt_cod_prod);
            recycler_view_list = (RecyclerView) view.findViewById(R.id.my_recycler_view);
            cantidad_s  =   (Spinner) view.findViewById(R.id.cantidad_spi);
            /*String []cantidades={"0","1","2","3","4","5","6","7","8","9","10"};
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, cantidades);
            cantidad_s.setAdapter(adapter);
            cantidad_s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Log.e("Items del detalle", String.valueOf(items.size()));
                    if(detall.isEmpty()) {
                        detall.add(new Detalles(cantidad_s.getSelectedItem().toString(), codigo.getText().toString(), precio.getText().toString(), cod_prod.getText().toString()));
                    }else{
                        int aux=0;
                        for (int x=0; x<detall.size(); x++){
                            if(detall.get(x).getCodigo().equals(codigo.getText())){
                                detall.set(x,new Detalles(cantidad_s.getSelectedItem().toString(), codigo.getText().toString(), precio.getText().toString(), cod_prod.getText().toString()));
                                aux++;
                            }
                        }
                        if(aux==0) {
                            detall.add(new Detalles(cantidad_s.getSelectedItem().toString(), codigo.getText().toString(), precio.getText().toString(), cod_prod.getText().toString()));
                        }
                     }

                    //IMPRIMIR TOTAL DE LOS RUBROS
                    contTotal=0;
                    for (int y=0; y<detall.size(); y++){
                        contTotal=contTotal+(Double.parseDouble(detall.get(y).getCantidad())*Double.parseDouble(detall.get(y).getPrecio()));
                        total.setText(String.valueOf(contTotal));
                    }

                    for (int x=0 ; x<detall.size(); x++){
                        if(detall.get(x).getCantidad().equals("0")){

                        }else{
                            Log.e("COL_LIB",detall.get(x).getCodigo()+"@@"+detall.get(x).getCod_prod()+"@@"+detall.get(x).getPrecio()+"@@"+detall.get(x).getCantidad());
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

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
        holder.cod_prod.setText(items.get(position).getCod_prod());

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.mContext, android.R.layout.simple_spinner_item, cantidades);
        holder.cantidad_s.setAdapter(adapter);
        holder.cantidad_s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e("Items del detalle", String.valueOf(items.size()));
                if(detall.isEmpty()) {
                    detall.add(new Detalles(holder.cantidad_s.getSelectedItem().toString(), holder.codigo.getText().toString(), holder.precio.getText().toString(), holder.cod_prod.getText().toString()));
                }else{
                    int aux=0;
                    for (int x=0; x<detall.size(); x++){
                        if(detall.get(x).getCodigo().equals(holder.codigo.getText())){
                            detall.set(x,new Detalles(holder.cantidad_s.getSelectedItem().toString(), holder.codigo.getText().toString(), holder.precio.getText().toString(), holder.cod_prod.getText().toString()));
                            aux++;
                        }
                    }
                    if(aux==0) {
                        detall.add(new Detalles(holder.cantidad_s.getSelectedItem().toString(), holder.codigo.getText().toString(), holder.precio.getText().toString(), holder.cod_prod.getText().toString()));
                    }
                }

                //IMPRIMIR TOTAL DE LOS RUBROS
                contTotal=0;
                for (int y=0; y<detall.size(); y++){
                    contTotal=contTotal+(Double.parseDouble(detall.get(y).getCantidad())*Double.parseDouble(detall.get(y).getPrecio()));
                    total.setText(String.valueOf(contTotal));
                }

                for (int x=0 ; x<detall.size(); x++){
                    if(detall.get(x).getCantidad().equals("0")){

                    }else{
                        Log.e("COL_LIB",detall.get(x).getCodigo()+"@@"+detall.get(x).getCod_prod()+"@@"+detall.get(x).getPrecio()+"@@"+detall.get(x).getCantidad());
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
    @Override
    public int getItemCount() {        return items.size();    }
}
