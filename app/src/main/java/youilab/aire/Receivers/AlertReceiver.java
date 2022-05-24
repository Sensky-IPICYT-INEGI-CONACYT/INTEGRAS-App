package youilab.aire.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import youilab.aire.AppTools.GPS;
import youilab.aire.processingdata.getFileJsonAir;
import youilab.aire.AppTools.NetworkStatus;


public class AlertReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

            //obtención de datos sobre la calidad del aire
            if (NetworkStatus.getStatus(context) == 3)
                new getFileJsonAir("http://youilab.ipicyt.edu.mx/ciudades-prosperas/assets/data/analysis.json");

    }
}

