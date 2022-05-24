package youilab.aire.AppTools;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.location.LocationManager;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import youilab.aire.MVP.Interfaces.LocationView;
import youilab.aire.processingdata.parserJSONAirAnalysisInformation;
import youilab.aire.R;
import youilab.aire.adapters.StationAdapter;
import youilab.aire.IntegrASCore.Stations.Station;
import youilab.aire.IntegrASCore.Nets.Net;
import youilab.aire.widget;


public class GPS {
    private static final String TAG = GPS.class.getSimpleName();

    //constantes de permisos
    final static int FINE_LOCATION_PERMISSION = 1;
    final static int COARSE_LOCATION_PERMISSION = 2;


    //location updates interval - 10sec
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 120000;

    //fastest updates interval - 5 sec
    //location updates will be received if another app is requesting the location
    //than your app can handle
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 30000;

    //desplazamiento minimo para que se actualice la ubicación
    private static final long MIN_DISPLACEMENT_IN_METERS = 0;

    private static final int REQUEST_CHECK_SETTINGS = 100;
    //bunch of location related apis
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;


    private Context aplicationContext;
    private Activity activityCompat=null;
    private LocationView view=null;




    //Boolean flag to toggle the ui
    private Boolean mRequestingLocationUpdates;

    //Acquire a reference to the system Location Manager
    LocationManager locationManager;


    public GPS(Context context){
        //  checkPermission(Manifest.permission.ACCESS_FINE_LOCATION,FINE_LOCATION_PERMISSION);
        this.aplicationContext=context;
        this.activityCompat=null;

        this.view = null;

        //Si el permiso esta habilitado
        init();
    }

    public GPS(Context context, LocationView view){
        //  checkPermission(Manifest.permission.ACCESS_FINE_LOCATION,FINE_LOCATION_PERMISSION);
        this.aplicationContext=context;
        this.activityCompat=null;
        this.view = view;

        //Si el permiso esta habilitado
        init();
    }

    private void init()
    {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(aplicationContext);
        mSettingsClient = LocationServices.getSettingsClient(aplicationContext);

        mLocationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                mCurrentLocation = locationResult.getLastLocation();

                if(view != null)
                {
                    view.currentLocation(mCurrentLocation);
                }
            }
        };

        mRequestingLocationUpdates = false;

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(MIN_DISPLACEMENT_IN_METERS);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }







    private void ActualizarDatosWidget()
    {
        AppWidgetManager mAppWidgetManager =
                aplicationContext.getSystemService(AppWidgetManager.class);

        Intent intent = new Intent(aplicationContext, widget.class);
        intent.setAction(mAppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] ids = mAppWidgetManager.getAppWidgetIds(new ComponentName(aplicationContext,widget.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,ids);



        aplicationContext.sendBroadcast(intent);
    }
    /**
     * Starting location updates
     * Check whether location settings are satisfied and then
     * location updates will be requested
     */
    private void startLocationUpdates()
    {
        mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener( new OnSuccessListener<LocationSettingsResponse>() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        Log.i(TAG,"All location settings are satisfied.");


                        //noinspection MissingPermission
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest,mLocationCallback, Looper.myLooper());

                        if(view!=null&&mCurrentLocation!=null)
                            view.currentLocation(mCurrentLocation);
                    }
                })
                .addOnFailureListener( new OnFailureListener() {
                    @Override
                    public void onFailure( Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode)
                        {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                Log.i(TAG,"Location settings are not satisfied. Attempting to upgrade "+ "location settings ");

                            try {
                                //Show the dialog by calling startResolutionForREsult(), and check the
                                //result in onActivityResult().
                                ResolvableApiException rae = (ResolvableApiException) e;
                                rae.startResolutionForResult(activityCompat, REQUEST_CHECK_SETTINGS);
                            }catch (IntentSender.SendIntentException loc){
                                loc.printStackTrace();
                            }


                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Loaction settings are inadequate, and cannot be "+
                                        "fixed here. Fix in settings,";

                                Log.e(TAG,errorMessage);

                                //Toast.makeText(activityCompat,errorMessage,Toast.LENGTH_LONG).show();
                        }

                    }
                });
    }

    public void startLocation()
    {
        mRequestingLocationUpdates = true;
        startLocationUpdates();
    }

    public void stopLocation()
    {
        mRequestingLocationUpdates= false;
        stopLocationUpdates();
    }

    private void stopLocationUpdates()
    {
        //Removing location updates
        mFusedLocationClient
                .removeLocationUpdates(mLocationCallback)
                .addOnCompleteListener( new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete( Task<Void> task) {
                        Log.i("GPS","Location updates stopped!");
                    }
                });
    }



}
