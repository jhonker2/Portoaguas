package Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pmat_programador_1.portoaguas.R;

import java.util.ArrayList;

import Models.Detalles;
import Models.Rubros;
import Models.SpinnerCant;

import static com.example.pmat_programador_1.portoaguas.MapsActivity.total;

/**
 * Created by PMAT-PROGRAMADOR_1 on 13/04/2017.
 */

    public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ItemRowHolder> {
    public ArrayList<Detalles> detall= new ArrayList<Detalles>();
    public ArrayList<Rubros> listObjeto;
    public double contTotal=0;
    private Context mContext;
    private ArrayList<Rubros> items;
    private String []cantidades={"0","1","2","3","4","5","6","7","8","9","10"};
    public String[][] data = new String[30][4];
    ///data[0][0]="0";


    public RecycleViewAdapter(Context context,ArrayList<Rubros> rubros  ){
        this.items=rubros;
        this.mContext = context;
    }


    class ItemRowHolder extends ViewHolder implements View.OnClickListener {

        protected TextView titulo, precio,codigo, cod_prod,cant;
       // final public Spinner cantidad_s;
        protected RecyclerView recycler_view_list;
        public CardView card;
        public Button min,plus;


        public ItemRowHolder(View view, ArrayList<Rubros> datos)  {
                super(view);
                this.codigo = (TextView) view.findViewById(R.id.codigo);
                this.titulo = (TextView) view.findViewById(R.id.card_titulo);
                this.precio = (TextView) view.findViewById(R.id.card_precio_val);
                this.cod_prod = (TextView) view.findViewById(R.id.txt_cod_prod);
                this.recycler_view_list = (RecyclerView) view.findViewById(R.id.my_recycler_view);
                this.min    =(Button) view.findViewById(R.id.btn_min);
                this.plus   =(Button) view.findViewById(R.id.btn_plus);
                this.cant   =(EditText) view.findViewById(R.id.txt_cant);
                //this.cantidad_s = (Spinner) view.findViewById(R.id.cantidad_spi);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, cantidades);
                //adapter.notifyDataSetChanged();
                //this.cantidad_s.setAdapter(adapter);
                this.card =(CardView) view.findViewById(R.id.cardPrincipal);
                listObjeto=datos;
                //this.cantidad_s.setOnItemSelectedListener(this);
                /*this.card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(v.getContext(),"Mensaje", Toast.LENGTH_LONG).show();
                    }
                });*/
                min.setOnClickListener(this);
                plus.setOnClickListener(this);
                this.cant.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        data[getAdapterPosition()][0] = s.toString();
                        Log.d("DATA " + getAdapterPosition(), s.toString());
                    }
                });

        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            Rubros ob = items.get(pos);

            if(v.getId()==min.getId()){
                //Toast.makeText(min.getContext(),"Boton Min "+pos+"",Toast.LENGTH_LONG).show();
                String cantidad = cant.getText().toString();
                    if(cantidad.equals("")){
                        cantidad="0";
                    }
                int jlh = Integer.valueOf(cantidad);
                cant.setText(String.valueOf(jlh - 1 >= 0 ? jlh - 1 : 0));
                if(detall.isEmpty()) {
                    detall.add(new Detalles(cant.getText().toString(), codigo.getText().toString(), precio.getText().toString(),cod_prod.getText().toString()));
                }else{
                    int aux=0;
                    for (int x=0; x<detall.size(); x++){
                        if(detall.get(x).getCodigo().equals(codigo.getText())){
                            detall.set(x,new Detalles(cant.getText().toString(), codigo.getText().toString(),precio.getText().toString(), cod_prod.getText().toString()));
                            aux++;
                        }
                    }
                    if(aux==0) {
                        detall.add(new Detalles(cant.getText().toString(), codigo.getText().toString(),precio.getText().toString(), cod_prod.getText().toString()));
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
                        //holder.cantidad_s.setSelection(0);
                    }else{
                        Log.e("COL_LIB",detall.get(x).getCodigo()+"@@"+detall.get(x).getCod_prod()+"@@"+detall.get(x).getPrecio()+"@@"+detall.get(x).getCantidad());
                    }
                }

            }else{
               // Toast.makeText(min.getContext(),"Boton Mas "+pos+"",Toast.LENGTH_LONG).show();
                String cantidad = cant.getText().toString();
                if(cantidad.equals("")){
                    cantidad="0";
                }
                int jlh = Integer.valueOf(cantidad);
                cant.setText(String.valueOf(jlh+1));
                if(detall.isEmpty()) {
                    detall.add(new Detalles(cant.getText().toString(), codigo.getText().toString(), precio.getText().toString(),cod_prod.getText().toString()));
                }else{
                    int aux=0;
                    for (int x=0; x<detall.size(); x++){
                        if(detall.get(x).getCodigo().equals(codigo.getText())){
                            detall.set(x,new Detalles(cant.getText().toString(), codigo.getText().toString(),precio.getText().toString(), cod_prod.getText().toString()));
                            aux++;
                        }
                    }
                    if(aux==0) {
                        detall.add(new Detalles(cant.getText().toString(), codigo.getText().toString(),precio.getText().toString(), cod_prod.getText().toString()));
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
                        //holder.cantidad_s.setSelection(0);
                    }else{
                        Log.e("COL_LIB",detall.get(x).getCodigo()+"@@"+detall.get(x).getCod_prod()+"@@"+detall.get(x).getPrecio()+"@@"+detall.get(x).getCantidad());
                    }
                }
            }

        }

        /*
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            int pos= getAdapterPosition();
            Log.e("posicion ", String.valueOf(pos));
            Log.e("POSICION", String.valueOf(position));
            //Rubros objeto = listObjeto.get(pos);
            if(detall.isEmpty()) {
                detall.add(new Detalles(this.cantidad_s.getSelectedItem().toString(), this.codigo.getText().toString(), this.precio.getText().toString(), this.cod_prod.getText().toString()));
            }else{
                int aux=0;
                for (int x=0; x<detall.size(); x++){
                    if(detall.get(x).getCodigo().equals(this.codigo.getText())){
                        detall.set(x,new Detalles(this.cantidad_s.getSelectedItem().toString(), this.codigo.getText().toString(), this.precio.getText().toString(), this.cod_prod.getText().toString()));
                        aux++;
                    }
                }
                if(aux==0) {
                    detall.add(new Detalles(this.cantidad_s.getSelectedItem().toString(), this.codigo.getText().toString(),this.precio.getText().toString(), this.cod_prod.getText().toString()));
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
                    //holder.cantidad_s.setSelection(0);
                }else{
                    Log.e("COL_LIB",detall.get(x).getCodigo()+"@@"+detall.get(x).getCod_prod()+"@@"+detall.get(x).getPrecio()+"@@"+detall.get(x).getCantidad());
                }
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }*/


    }

    @Override
    public RecycleViewAdapter.ItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_details_cards, parent,false);
        ItemRowHolder mh = new ItemRowHolder(v ,items);
        return mh;
    }

    @Override
    public void onBindViewHolder(final ItemRowHolder holder, int position) {
        final int pos = position;

       // holder.itemView.setTag(items.get(position));

        holder.codigo.setText(items.get(position).getCodigo());
        holder.titulo.setText(items.get(position).getTitulos());
        holder.precio.setText(items.get(position).getPrecio());
        holder.cod_prod.setText(items.get(position).getCod_prod());
        holder.cant.setText(data[position][0]);
        //holder.cantidad_s.setTag(pos);
        /*holder.cantidad_s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               // Log.e("Items del detalle", String.valueOf(items.size()));
               // Log.e("S",String.valueOf(id));

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
                        detall.add(new Detalles(holder.cantidad_s.getSelectedItem().toString(), holder.codigo.getText().toString(),holder.precio.getText().toString(), holder.cod_prod.getText().toString()));
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
                        //holder.cantidad_s.setSelection(0);
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
    @Override
    public int getItemCount() {return items.size();}
}
