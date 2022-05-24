package youilab.aire;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.TextView;

import youilab.aire.AppTools.GPS;
import youilab.aire.IntegrASCore.Nets.Net;
import youilab.aire.IntegrASCore.Stations.Station;
import youilab.aire.MVP.Interfaces.CurrentNet.CurrentNetPresenter;
import youilab.aire.MVP.Interfaces.CurrentNet.CurrentNetView;
import youilab.aire.MVP.Interfaces.LocationView;
import youilab.aire.MVP.Presenters.CurrentNetPresenterImpl;
import youilab.aire.adapters.StationAdapter;

import java.util.List;

public class principal extends AppCompatActivity implements LocationView, CurrentNetView {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private GPS myGPS;

    private TextView NombreRed,CalidadAire,fechaDeDatos,tiempoDatos,txtIndiceAire;

    private ImageView imgStatus;

    private  CardView bodyStatus;

     RecyclerView recyclerViewStations;


     private CurrentNetPresenter currentNetPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_principal_aire);


         CalidadAire = (TextView) findViewById(R.id.lbValueStatusAir);
         NombreRed = (TextView) findViewById(R.id.lbValueSourceAir);
         fechaDeDatos = (TextView) findViewById(R.id.date);
         tiempoDatos = (TextView) findViewById(R.id.time);

         bodyStatus = (CardView) findViewById(R.id.cvInfoAir);
        imgStatus = (ImageView) findViewById(R.id.icon_state_air);
        final CardView cvScaleRisk = (CardView) findViewById(R.id.cvScaleRisk);

        final FloatingActionButton fabListNets = (FloatingActionButton) findViewById(R.id.fabListNets);
         recyclerViewStations = (RecyclerView) findViewById(R.id.rvStations);

        fabListNets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(principal.this, RedesMonitorio.class);
                startActivity(intent);
            }
        });

        cvScaleRisk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(principal.this, AirRiskPop.class));
            }
        });

        myGPS = new GPS( this,this);
        myGPS.startLocation();

        currentNetPresenter = new CurrentNetPresenterImpl(this);

    }

    @Override
    protected void onStop() {
        super.onStop();
        myGPS.stopLocation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        myGPS.stopLocation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        myGPS.startLocation();
    }


    @Override
    public void currentLocation(Location currentLocation) {
        Log.i("Loaction", "lat: "+currentLocation.getLatitude() + " lng: "+ currentLocation.getLongitude());
        currentNetPresenter.getCurrentNet(currentLocation);
    }


    @Override
    public void currentNet(Net stationsNet) {
        NombreRed.setText(stationsNet.getName());
        CalidadAire.setText(stationsNet.getStatusNameNet());

        String[] datosDate = stationsNet.getFecha().split("T");
        fechaDeDatos.setText(datosDate[0]);
        tiempoDatos.setText(datosDate[1].substring(0,5)+" hrs");

        changeColors(stationsNet.getStatusNet());
        showInformation(stationsNet.getStatusNet());
        loadStations(stationsNet);
    }

    private void changeColors(int statusAir)
    {

        if(statusAir==2){
            bodyStatus.setCardBackgroundColor(getResources().getColor(R.color.aceptable));
            imgStatus.setImageDrawable(getDrawable(R.drawable.marcador_amarillo));
        }else  if(statusAir==3){
            bodyStatus.setCardBackgroundColor(getResources().getColor(R.color.mala));
            imgStatus.setImageDrawable(getDrawable(R.drawable.marcador_naranja));
        }else
        if(statusAir==4){
            bodyStatus.setCardBackgroundColor(getResources().getColor(R.color.muy_mala));
            imgStatus.setImageDrawable(getDrawable(R.drawable.marcador_rojo));
        }else  if(statusAir==5){
            bodyStatus.setCardBackgroundColor(getResources().getColor(R.color.extremadamente_mala));
            imgStatus.setImageDrawable(getDrawable(R.drawable.marcador_morado));
        }else  if(statusAir==6){
            bodyStatus.setCardBackgroundColor(getResources().getColor(R.color.greylight));
            imgStatus.setImageDrawable(getDrawable(R.drawable.marcador_blanco));
        }else  if(statusAir==1){
            bodyStatus.setCardBackgroundColor(getResources().getColor(R.color.bien));
            imgStatus.setImageDrawable(getDrawable(R.drawable.marcador_verde));
        }
    }

    private void showInformation(int statusAir)
    {
        CardView cvBien;
        CardView cvAceptable;
        CardView cvMala;
        CardView cvMuyMala;
        CardView cvExtremadamenteMala;

        cvBien = findViewById(R.id.lytStatus_1);
        cvAceptable = findViewById(R.id.lytStatus_2);
        cvMala = findViewById(R.id.lytStatus_3);
        cvMuyMala = findViewById(R.id.lytStatus_4);
        cvExtremadamenteMala = findViewById(R.id.lytStatus_5);

        cvBien.setVisibility(View.GONE);
        cvAceptable.setVisibility(View.GONE);
        cvMala.setVisibility(View.GONE);
        cvMuyMala.setVisibility(View.GONE);
        cvExtremadamenteMala.setVisibility(View.GONE);

        if(statusAir==1)
            cvBien.setVisibility(View.VISIBLE);
        else if(statusAir==2)
            cvAceptable.setVisibility(View.VISIBLE);
        else if(statusAir==3)
            cvMala.setVisibility(View.VISIBLE);
        else if(statusAir==4)
            cvMuyMala.setVisibility(View.VISIBLE);
        else if (statusAir==5)
            cvExtremadamenteMala.setVisibility(View.VISIBLE);

    }

    private void loadStations(Net myNet){

        // set a StaggeredGridLayoutManager with 3 number of columns and vertical orientation
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        recyclerViewStations.setLayoutManager(staggeredGridLayoutManager); // set LayoutManager to RecyclerView
        //  call the constructor of CustomAdapter to send the reference and data to Adapter
        StationAdapter customAdapter = new StationAdapter(myNet.getStationList(),this,myNet.getCodeNet());
        recyclerViewStations.setAdapter(customAdapter); // set the Adapter to RecyclerView
    }
}