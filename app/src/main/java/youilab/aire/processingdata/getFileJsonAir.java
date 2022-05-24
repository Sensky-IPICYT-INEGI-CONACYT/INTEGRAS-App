package youilab.aire.processingdata;

import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

public class getFileJsonAir {
    private static File archivoNuevo;
//"http://youilab.ipicyt.edu.mx/ciudades-prosperas/assets/data/analysis.json"
    public getFileJsonAir(String url)
    {


        downloadJsonhandler upload = new downloadJsonhandler();
        upload.execute("Air",url);
    }

    private class downloadJsonhandler extends AsyncTask<String, Void, String>
    {

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            File folder = createDirectory(params[0]);
            try {
                //  Log.i("Direccion",params[1]);
                getJsonAnalisys(folder, new URL(params[1]));
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return folder.getAbsolutePath();
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            File archivoTmp = new File (result+File.separator+"tmpejemplo2.json");
            if(archivoTmp.exists() && archivoTmp.length()>0)
            {
                File archivoOriginal = new File (result+File.separator+"ejemplo2.json");
                if(!archivoOriginal.exists()) {
                    try {
                        archivoOriginal.createNewFile();
                    }catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
                archivoTmp.renameTo(archivoOriginal);
                archivoTmp.delete();

            }
        }

        private void getJsonAnalisys(File folder,URL url) throws IOException
        {
            archivoNuevo = new File(folder, File.separator+"tmpejemplo2.json");
            if(!archivoNuevo.exists()){
                archivoNuevo.createNewFile();
                FileUtils.copyURLToFile(url, archivoNuevo);
            }else{
                archivoNuevo.delete();
                archivoNuevo.createNewFile();
                FileUtils.copyURLToFile(url, archivoNuevo);
            }
        }

        public File createDirectory(String name)
        {
            File folder = new File(Environment.getExternalStorageDirectory(), "/"+name);
            if (!folder.exists())
                folder.mkdir();
            return folder;
        }
    }
}
