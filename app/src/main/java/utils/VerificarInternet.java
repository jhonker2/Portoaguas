package utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.io.IOException;

/**
 * Created by PMAT-PROGRAMADOR_1 on 24/07/2017.
 */

public class VerificarInternet extends AsyncTask<Void, Void, Boolean> {

    public interface EntoncesHacer{
        void cuandoHayInternet();
        void cuandoNOHayInternet();
    }

    private ProgressDialog dialog;
    private Context context;
    private EntoncesHacer accion;

    public VerificarInternet(Context context, EntoncesHacer accion) {
        this.context = context;
        this.accion = accion;
    }

    /*@Override
    protected void onPreExecute() {
        dialog = new ProgressDialog(context);
        dialog.setMessage("Verificando conexion a Internet");
        dialog.setCancelable(false);
        dialog.show();
        super.onPreExecute();
    }*/

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);

       // dialog.dismiss();

        if(aBoolean){
            accion.cuandoHayInternet();
        }else{
            accion.cuandoNOHayInternet();
        }
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        Runtime runtime = Runtime.getRuntime();
        try{
          // java.lang.Process ipProcess = runtime.exec("ping -c 2 -w 4 192.168.137.1");
            java.lang.Process ipProcess = runtime.exec("ping -c 2 -w 4 google.com");
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        }catch (IOException e){
            e.printStackTrace();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        return false;
    }
}
