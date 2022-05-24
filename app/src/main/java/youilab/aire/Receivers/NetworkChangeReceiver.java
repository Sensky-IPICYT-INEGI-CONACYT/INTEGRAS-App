package youilab.aire.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import youilab.aire.AppTools.NetworkStatus;
import youilab.aire.processingdata.getFileJsonAir;

/**
 * Recibidor broadcast para el cambio de conexiones de red,
 * en este caso en el servicio llamada IntegrASIntentService que siemore esta activo vigilando
 * para ver si esta conectado al wifi y actulizar la información
 */
public class NetworkChangeReceiver extends BroadcastReceiver {
    /**
     * Recibe los cambios en las conexiones del dispositivo
     * @param context Contexto en el que se esta usando el bradcast
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {

        try
        {
            int bndConection = NetworkStatus.getStatus(context); //se obtiene el estado de la conexion de internet
            if (bndConection == NetworkStatus.WIFI_INTERNET_ACCCESS) {//si se tiene acceso a internet por Wifi se lanza el servicio de sincronización

                //descarga de la información de la calidad del aire
                new getFileJsonAir("http://youilab.ipicyt.edu.mx/ciudades-prosperas/assets/data/analysis.json");


                Toast.makeText(context,"descarga de datos",Toast.LENGTH_SHORT).show();
                /*
                Intent myService = new Intent(context, synchronizationService.class);//intent al servicio que sincroniza las evidencias
                myService.setAction(synchronizationService.ACTION_RUN_ISERVICE);//valor para que el servicio reconosca que accion va a realizar
                context.startService(myService);//se inicia el servicio

                 */
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}
