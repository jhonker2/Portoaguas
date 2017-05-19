package com.example.pmat_programador_1.portoaguas;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.EditText;
import android.widget.Toast;

import com.muddzdev.styleabletoastlibrary.StyleableToast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import utils.JSON;

public class loginActivity extends AppCompatActivity {
    public Button btnlogin;
    public EditText txt_usuario, txt_clave;
    public static String data;
    public String resul;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences dato = this.getSharedPreferences("perfil", Context.MODE_PRIVATE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (dato.getString("p_nombreU", null) != null) {
            startActivity(new Intent(loginActivity.this, MainActivity.class));
            finish();
        }else {
            btnlogin = (Button) findViewById(R.id.btn_login);
            txt_usuario = (EditText) findViewById(R.id.textUsuario);
            txt_clave = (EditText) findViewById(R.id.textClave);


            btnlogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (txt_usuario.getText().toString().equals("") && txt_clave.getText().toString().equals("")) {
                        StyleableToast.makeText(loginActivity.this, "Usuario y Contraseña se encuentra vacio!", Toast.LENGTH_LONG, R.style.StyledToastError).show();
                    } else if (txt_usuario.getText().toString().equals("")) {
                        StyleableToast.makeText(loginActivity.this, "El nombre de usuario se encuentra vacio!", Toast.LENGTH_LONG, R.style.StyledToastError).show();
                    } else if (txt_clave.getText().toString().equals("")) {
                        StyleableToast.makeText(loginActivity.this, "La contraseña se encuentra vacio!", Toast.LENGTH_LONG, R.style.StyledToastError).show();
                    } else {
                        Log.e("ID MOVIL", Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID));
                        new RegistrarDispositivos().execute(txt_usuario.getText().toString(), txt_clave.getText().toString());
                    }


                }
            });

        }
    }

    class RegistrarDispositivos extends AsyncTask<String, Void, String> {
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
        protected void onPostExecute(String aBoolean) {
            pDialog.dismiss();
            if(aBoolean.equals("Ecedula")){
                StyleableToast.makeText(loginActivity.this, "La cedula ingresada no se encuentra registrada!!", Toast.LENGTH_LONG, R.style.StyledToastError).show();
                txt_usuario.setError("Usuario no existe");
                txt_usuario.requestFocus();
                //Intent inte =  new Intent(loginActivity.this,MainActivity.class);
                //startActivity(inte);
                //finish();
            }else if(aBoolean.equals("Eclave")) {
                    StyleableToast.makeText(loginActivity.this, "La clave es incorrecta!!", Toast.LENGTH_LONG, R.style.StyledToastError).show();
                    txt_clave.setError("Clave incorrecta");
                    txt_clave.requestFocus();
                    txt_clave.setText("");
            }else if(aBoolean.equals("Eduplicado")) {
                StyleableToast.makeText(loginActivity.this, "El usuario ya ha iniciado Sesion en otro dispositivo!! No es posible iniciar Sesión", Toast.LENGTH_LONG, R.style.StyledToastError).show();
            }else if(aBoolean.equals("Login Correcto")){
                StyleableToast.makeText(loginActivity.this, "Login Correcto Bienvenido!!", Toast.LENGTH_LONG, R.style.StyledToast).show();
                Intent inte =  new Intent(loginActivity.this,MainActivity.class);
                startActivity(inte);
                finish();
            }
        }

        @Override
        protected void onCancelled(String s) {
            pDialog.dismiss();

        }

        @Override
        protected String doInBackground(String... strings) {
            SharedPreferences dato = getSharedPreferences("perfil", Context.MODE_PRIVATE);

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("id_Dispositivo", Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID)));
            nameValuePairs.add(new BasicNameValuePair("cedula", strings[0]));
            nameValuePairs.add(new BasicNameValuePair("clave", strings[1]));

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://"+ JSON.ipserver+"/login");
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
                HttpResponse response = httpclient.execute(httppost);
                String status= String.valueOf(response.getStatusLine().getStatusCode());
                Log.e("Estado",status);
                HttpEntity entity = response.getEntity();
                data = EntityUtils.toString(entity);
                //Log.e("LOGIN", data);
                JSONObject obj = new JSONObject(data);
                String respuesta = obj.getString("respuesta");
                String id_usuario = obj.getString("id_usu");
                String nombre = obj.getString("nombre_usu");
                String cargo = obj.getString("cargo_usu");


                //OBTENER LOS DATOS PARA LA PREFERENCIA
                Log.e("Datos",id_usuario+" "+cargo+" "+nombre);

                SharedPreferences.Editor editor = dato.edit();
               editor.putString("p_idUsuario", id_usuario);
                editor.putString("p_nombreU", nombre);
                editor.putString("p_cargoU", cargo);
                editor.commit();
               resul=respuesta;

            }catch (UnsupportedEncodingException e){
                e.printStackTrace();
                resul="";
            }
            catch (ClientProtocolException e){
                Log.e("ClienteProtocol", "Error in http connection " + e.toString());
                resul="";
            } catch (Exception e ) {
                Log.e("log_tag", "Error in http connection " + e.toString());
                resul="";
            }
            return resul;
        }
    }

    }
