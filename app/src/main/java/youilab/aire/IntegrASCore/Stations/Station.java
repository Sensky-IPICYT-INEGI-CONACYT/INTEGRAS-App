package youilab.aire.IntegrASCore.Stations;

import android.location.Location;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Station {
    int idStation;
    String[] airQualityTags;
    String name;
    String state;
    Location stationLocation;
    int calidadDelAire;
    String fecha;

    String promMovil24SO2;
    String promMovil12PM10;
    String promMovil12PM25;
    String promMovil8CO;
    String promMovil8O3;

    String promHorariaNO2;
    String promHorariaO3;

    public Station(){}

    public void setIdStation(int idStation) {
        this.idStation = idStation;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setState(String state) {
        this.state = state;
    }
    public void setStationLocation(Location stationLocation) {
        this.stationLocation = stationLocation;
    }
    public void setAirQualityTags(String[] airQualityTags) {
        this.airQualityTags = airQualityTags;
    }
    public void setCalidadDelAire(int calidadDelAire) {
        this.calidadDelAire = calidadDelAire;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getIdStation() {
        return idStation;
    }

    public String getName() {
        return name;
    }
    public String getState() {
        return state;
    }
    public Location getStationLocation() {
        return stationLocation;
    }
    public String[] getAirQualityTags() {
        return airQualityTags;
    }
    public int getCalidadDelAire() {
        return calidadDelAire;
    }

    public String getFecha() {
        return fecha;
    }


    public void setPromHorariaNO2(String promHorariaNO2) {
        this.promHorariaNO2 = validateData(promHorariaNO2);
    }
    public void setPromHorariaO3(String promHorariaO3) {
        this.promHorariaO3 = validateData(promHorariaO3);
    }
    public void setPromMovil8CO(String promMovil8CO) {
        this.promMovil8CO = validateData(promMovil8CO);
    }
    public void setPromMovil8O3(String promMovil8O3) {
        this.promMovil8O3 = validateData(promMovil8O3);
    }
    public void setPromMovil12PM10(String promMovil12PM10) {
        this.promMovil12PM10 = validateData(promMovil12PM10);
    }



    public void setPromMovil12PM25(String promMovil12PM25) {
        this.promMovil12PM25 = validateData(promMovil12PM25);
    }
    public void setPromMovil24SO2(String promMovil24SO2) {
        this.promMovil24SO2 = validateData(promMovil24SO2);
    }


    public String getPromHorariaNO2() {
        return promHorariaNO2;
    }
    public String getPromHorariaO3() {
        return promHorariaO3;
    }
    public String getPromMovil8CO() {
        return promMovil8CO;
    }
    public String getPromMovil8O3() {
        return promMovil8O3;
    }
    public String getPromMovil12PM10() {
        return promMovil12PM10;
    }
    public String getPromMovil12PM25() {
        return promMovil12PM25;
    }
    public String getPromMovil24SO2() {
        return promMovil24SO2;
    }


    private String validateData(String data){
        if(Double.valueOf(data)==-1)
            return "SD";
        else
            return data;
    }
}
