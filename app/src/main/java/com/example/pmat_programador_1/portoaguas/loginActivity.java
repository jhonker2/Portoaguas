package com.example.pmat_programador_1.portoaguas;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.muddzdev.styleabletoastlibrary.StyleableToast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;

public class loginActivity extends AppCompatActivity {
    public Button btnlogin;
    public static String data;
    public boolean resul;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnlogin = (Button) findViewById(R.id.btn_login);



        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RegistrarDispositivos().execute();


            }
        });


    }

    class RegistrarDispositivos extends AsyncTask<String, Void, Boolean> {
        private ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(loginActivity.this);
            pDialog.setMessage("Validando Datos del Usuario...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if(aBoolean==true){
                pDialog.dismiss();
                StyleableToast.makeText(loginActivity.this, "Login Correcto!!", Toast.LENGTH_SHORT, R.style.StyledToast).show();
                Intent inte =  new Intent(loginActivity.this,MainActivity.class);
                startActivity(inte);
                finish();
            }else{
                pDialog.dismiss();
                StyleableToast.makeText(loginActivity.this, "Error al ingresar al sistema!!", Toast.LENGTH_SHORT, R.style.StyledToastError).show();

            }
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("id_Dispositivo", Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID)));
            nameValuePairs.add(new BasicNameValuePair("estado", "online"));

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://192.168.137.1:8090/portal-portoaguas/public/Dispositivo");
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                data = EntityUtils.toString(entity);
                Log.e("DISPOSITIVO", data);

                //JSONObject obj= new JSONObject(data);
                //String  codigojson=obj.getString("registro");
                //data=codigojson;
                resul=true;
            } catch (Exception e) {
                Log.e("log_tag", "Error in http connection " + e.toString());
                resul=false;
            }
            return resul;
        }
    }

    }
