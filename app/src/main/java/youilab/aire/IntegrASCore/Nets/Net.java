package youilab.aire.IntegrASCore.Nets;

import android.app.Service;
import android.location.Location;

import java.util.ArrayList;
import java.util.List;

import youilab.aire.IntegrASCore.Stations.Station;


public class Net {
    String codeNet;
    String name;
    Location netLocation;
    List<Station> stationList ;
    int StatusNet;
    String statusNameNet;
    String fecha;
    public Net(){
        stationList = new ArrayList<>();
        netLocation = new Location(Service.LOCATION_SERVICE);
        netLocation.setLongitude(0.0);
        netLocation.setLatitude(0.0);
    }



    public void setCodeNet(String codeNet) {
        this.codeNet = codeNet;
    }
    public String getCodeNet() {
        return codeNet;
    }

    public void addStation(Station station) {

        this.stationList.add(station);
        calculateNewPositionOfNet();
    }
    public List<Station> getStationList() {
        return stationList;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setStatusNet(int statusNet) {
        this.StatusNet = statusNet;
        if(statusNet==1)
            this.setStatusNameNet("Buena");
        else if(statusNet==2)
            this.setStatusNameNet("Aceptable");
        else if(statusNet==3)
            this.setStatusNameNet("Mala");
        else if(statusNet==4)
            this.setStatusNameNet("Muy Mala");
        else if(statusNet==5)
            this.setStatusNameNet("Extremadamente Mala");
        else if(statusNet==6)
            setStatusNameNet("No disponible");
    }
    public int getStatusNet() {
        return StatusNet;
    }

    public void setStatusNameNet(String statusNameNet) {
        this.statusNameNet = statusNameNet;
    }
    public String getStatusNameNet() {
        return statusNameNet;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getFecha() {
        return fecha;
    }

    public void setNewLat(Double newLat) {
        this.netLocation.setLatitude(newLat);
    }
    public void setNetLng(Double newLng){
        this.netLocation.setLongitude(newLng);
    }
    public Location getNetLocation() {
        return netLocation;
    }
    private void calculateNewPositionOfNet()
    {
        Double sizeOfList = Double.valueOf( this.stationList.size());
        Double newLat=0.0;
        Double newLng=0.0;
        boolean bndNotAccess=true;
        int staturAir=1;
        for (Station station : this.stationList
        ) {
            newLat=newLat+station.getStationLocation().getLatitude();
            newLng= newLng+ station.getStationLocation().getLongitude();

            if(station.getCalidadDelAire()!=6) {
                if (staturAir <= station.getCalidadDelAire()) {

                    this.StatusNet = station.getCalidadDelAire();
                    if (this.StatusNet == 1)
                        this.statusNameNet = "Buena";
                    else if (this.StatusNet == 2)
                        this.statusNameNet = "Aceptable";
                    else if (this.StatusNet == 3)
                        this.statusNameNet = "Mala";
                    else if (this.StatusNet == 4)
                        this.statusNameNet = "Muy Mala";
                    else if (this.StatusNet == 5)
                        this.statusNameNet = "Extremadamente Mala";


                    this.setFecha(station.getFecha());
                    this.setStatusNet(staturAir);
                    bndNotAccess=false;
                }

            }else
            {
                this.setFecha(station.getFecha());
            }



        }

        if(bndNotAccess)
        {
            this.StatusNet = 6;
            this.statusNameNet="No disponible";
        }
        this.netLocation.setLatitude(newLat/sizeOfList);
        this.netLocation.setLongitude(newLng/sizeOfList);
    }

}
