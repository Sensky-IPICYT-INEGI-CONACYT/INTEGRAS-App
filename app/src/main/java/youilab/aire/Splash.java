package youilab.aire;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

import androidx.core.content.ContextCompat;
import youilab.aire.AppTools.PermissionsIntegrAS;
import youilab.aire.Receivers.AlertReceiver;
import youilab.aire.Services.IntegrASIntentService;

public class Splash extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    private ImageView iv;
    TextView txtPermissions;
    static boolean bndLocationPermission=false;
    static boolean bndWriteESPermission=false;

    CardView cvPermissions;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        iv = (ImageView) findViewById(R.id.imagen);
        txtPermissions = (TextView) findViewById(R.id.txtPermissions);
        cvPermissions = (CardView) findViewById(R.id.btnPermissions);

        cvPermissions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               checkPermissions();
            }
        });

        Animation localAnimation = AnimationUtils.loadAnimation(Splash.this,R.anim.blink);
        iv.startAnimation(localAnimation);



    }

    @Override
    protected void onResume() {
        super.onResume();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkPermissions();
            }
        },1000);
    }

    @Override
    public void onRequestPermissionsResult(int paramInt, @NonNull String[] permissions, @NonNull int[] paramArrayOfInt) {
        switch (paramInt)
        {
            default:
                return;


            case 2:
                if ((paramArrayOfInt.length > 0) && (paramArrayOfInt[0] == PackageManager.PERMISSION_GRANTED))
                {
                   bndLocationPermission=true;
                    if(bndWriteESPermission)
                        txtPermissions.setText(getResources().getString(R.string.permissions_acepted));
                }
                return;

            case 1:
                if ((paramArrayOfInt.length > 0) && (paramArrayOfInt[0] == PackageManager.PERMISSION_GRANTED))
                {
                    bndWriteESPermission=true;
                    if( bndLocationPermission)
                        txtPermissions.setText(getResources().getString(R.string.permissions_acepted));
                }


        }
    }


    private void checkPermissions()
    {
        if(PermissionsIntegrAS.accesFineLocationPermission(this) && PermissionsIntegrAS.accessWriteESPermission(this)) {
            txtPermissions.setText(getResources().getString(R.string.permissions_acepted));
            //Crear sistema para refrescar la información
            Calendar c = Calendar.getInstance();
            startAlarm(c);

            //se lanza el servicio de monitoreo
            Intent serv = new Intent(this, IntegrASIntentService.class); //serv de tipo Intent
            ContextCompat.startForegroundService(this,serv);//este se lanza como un startforegrundservice ya que estará siempre activo
            //estará ligado a una notificación esta notificación debe lanzarse en menos de 5 segundos de iniciar ya que si no falla el enlace

            //abrir la vista principal
            Intent intentPrincipal = new Intent(Splash.this, principal.class);
            startActivity(intentPrincipal);
            finish();
        }


    }

    private void startAlarm(Calendar c){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,0, intent,PendingIntent.FLAG_UPDATE_CURRENT);


        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), 600000,pendingIntent);
        // alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);

    }


    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        Calendar c = Calendar.getInstance();
        //c.set(Calendar.HOUR_OF_DAY, i);
        //c.set(Calendar.MINUTE, i1);
        //c.set(Calendar.SECOND,0);

        startAlarm(c);


    }
}