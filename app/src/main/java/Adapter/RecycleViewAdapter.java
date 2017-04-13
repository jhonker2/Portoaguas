package Adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.pmat_programador_1.portoaguas.MapsActivity;
import com.example.pmat_programador_1.portoaguas.R;

import java.util.ArrayList;

import Models.Rubros;

/**
 * Created by PMAT-PROGRAMADOR_1 on 13/04/2017.
 */

    public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ItemRowHolder> {
    private ArrayList<Rubros> items;
    private Context mContext;
    public class ItemRowHolder extends RecyclerView.ViewHolder {

        protected TextView titulo, precio,cantidad;

        protected RecyclerView recycler_view_list;


        public ItemRowHolder(View view) {
            super(view);

            titulo = (TextView) view.findViewById(R.id.card_titulo);
            precio = (TextView) view.findViewById(R.id.card_precio_val);
            cantidad = (TextView) view.findViewById(R.id.card_cant_val);
            recycler_view_list = (RecyclerView) view.findViewById(R.id.my_recycler_view);
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
    public void onBindViewHolder(ItemRowHolder holder, int position) {
        holder.titulo.setText(items.get(position).getTitulos());
        holder.precio.setText(items.get(position).getPrecio());

    }

    @Override
    public int getItemCount() {
        return items.size();
    }



}
