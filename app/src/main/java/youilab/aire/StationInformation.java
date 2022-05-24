package youilab.aire;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.widget.TextView;

import java.util.List;

import youilab.aire.IntegrASCore.Nets.Net;
import youilab.aire.IntegrASCore.Stations.Station;
import youilab.aire.MVP.Interfaces.StationInformation.StationInformationPresenter;
import youilab.aire.MVP.Interfaces.StationInformation.StationInformationView;
import youilab.aire.MVP.Presenters.StationInformationPresenterImpl;
import youilab.aire.processingdata.parserJSONAirAnalysisInformation;

public class StationInformation extends AppCompatActivity implements StationInformationView {


    TextView txtPromMPM25,txtPromMPM10,txtPromM03,txtPromMSO2,txtPromMCO;
    TextView txtPromH03,txtPromHNO2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_information);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        txtPromH03 = (TextView) findViewById(R.id.txtPromHO3);
        txtPromHNO2 = (TextView) findViewById(R.id.txtPromHNO2);

        txtPromM03 = (TextView) findViewById(R.id.txtPromMO3);
        txtPromMCO = (TextView) findViewById(R.id.txtPromMCO);
        txtPromMSO2 = (TextView) findViewById(R.id.txtPromMSO2);
        txtPromMPM10 = (TextView) findViewById(R.id.txtPromMPM10);
        txtPromMPM25 = (TextView) findViewById(R.id.txtPromMPM25);

        Intent intent = getIntent();

        StationInformationPresenter stationInformationPresenter = new StationInformationPresenterImpl(this);
        stationInformationPresenter.getStationInformation(intent.getStringExtra("codeNet"),intent.getStringExtra("idStation"));

    }

    @Override
    protected void onResume() {
        super.onResume();

    }


    @Override
    public void StationInformation(Station station) {
        txtPromMPM25.setText(station.getPromMovil12PM25());
        txtPromMPM10.setText(station.getPromMovil12PM10());
        txtPromMSO2.setText(station.getPromMovil24SO2());
        txtPromMCO.setText(station.getPromMovil8CO());
        txtPromM03.setText(station.getPromMovil8O3());

        txtPromHNO2.setText(station.getPromHorariaNO2());
        txtPromH03.setText(station.getPromHorariaO3());
        getSupportActionBar().setTitle(station.getName());
    }

    @Override
    public void NetInformation(Net net) {
        getSupportActionBar().setSubtitle(net.getName());

    }
}
