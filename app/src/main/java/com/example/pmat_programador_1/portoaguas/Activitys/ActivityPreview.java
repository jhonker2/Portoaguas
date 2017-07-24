package com.example.pmat_programador_1.portoaguas.Activitys;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.example.pmat_programador_1.portoaguas.R;

import java.io.File;
import java.io.IOException;

import sqlit.Movimiento;

/**
 * Created by PMAT-PROGRAMADOR_1 on 24/07/2017.
 */

public class ActivityPreview extends AppCompatActivity {
    private ImageView imagenExtendida;
    private Movimiento itemDetallado;
    public File storageDir;
    public static final String EXTRA_PARAM_ID ="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.previewimagen);
        imagenExtendida = (ImageView) findViewById(R.id.imagen_extendida);
        Intent intent=getIntent();
        Bundle extras =intent.getExtras();
        if (extras != null) {//ver si contiene datos
            storageDir= new File(EXTRA_PARAM_ID);
            ContentResolver cr = this.getContentResolver();
            try {
                imagenExtendida.setImageBitmap(android.provider.MediaStore.Images.Media.getBitmap(cr, Uri.fromFile(storageDir)));
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
         getIntent().getIntExtra(EXTRA_PARAM_ID, 0);

    }
}
