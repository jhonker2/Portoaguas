package Adapter;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.pmat_programador_1.portoaguas.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import sqlit.Movimiento;

/**
 * Created by PMAT-PROGRAMADOR_1 on 12/04/2017.
 */

public class MovimientsAdapter2 extends BaseAdapter {
    protected Activity activity;
    protected ArrayList<Movimiento> items;
    public TextView codigo,idmedidor,estado;
    public ImageView img;
    public Movimiento item;
    public File storageDir;
    public Uri output;

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
        }
        item= items.get(position);
        codigo          = (TextView) vi.findViewById(R.id.t_id);
        idmedidor       = (TextView) vi.findViewById(R.id.t_idmedidor);
        estado          = (TextView) vi.findViewById(R.id.t_estado);
        img             = (ImageView) vi.findViewById(R.id.imgF);
        codigo.setText(String.valueOf(item.getId()));
        idmedidor.setText(item.getObservacion());
        estado.setText(item.getTotal_mov());
        storageDir= new File(item.getImagen());
        ContentResolver cr = activity.getContentResolver();
        /*Glide.with(img.getContext())
                .load(storageDir)
                .into(img);*/
        try {
            img.setImageBitmap(android.provider.MediaStore.Images.Media.getBitmap(cr, Uri.fromFile(storageDir)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return vi;
    }
}
