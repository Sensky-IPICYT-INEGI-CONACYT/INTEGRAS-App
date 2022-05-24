package youilab.aire.IntegrASCore.Stations;

import android.app.Service;
import android.location.Location;
import org.json.simple.JSONObject;

import java.util.List;

public class StationsDAOImpl implements StationsDAO{
    @Override
    public Station JSONToStation(JSONObject objt) {
        Station station = new Station();
        //se crea la primera estación de monitoreo
        station.setIdStation(Integer.valueOf(objt.get("IDSTATION").toString()));
        station.setName(objt.get("NAMESTATION").toString());
        station.setState(objt.get("STATE").toString());
        Location loc = new Location(Service.LOCATION_SERVICE);
        loc.setLatitude(Double.valueOf(objt.get("LAT").toString()));
        loc.setLongitude(Double.valueOf(objt.get("LNG").toString()));
        station.setStationLocation(loc);
        station.setCalidadDelAire(Integer.valueOf(objt.get("calidadDelAire").toString()));
        station.setFecha(objt.get("now").toString());

        station.setPromHorariaNO2(objt.get("promHorariaNO2").toString());
        station.setPromHorariaO3(objt.get("promHorariaO3").toString());

        station.setPromMovil8CO(objt.get("promMovil8CO").toString());
        station.setPromMovil8O3(objt.get("promMovil8O3").toString());

        station.setPromMovil24SO2(objt.get("promMovil24SO2").toString());

        station.setPromMovil12PM10(objt.get("promMovil12PM10").toString());
        station.setPromMovil12PM25(objt.get("promMovil12PM2.5").toString());

        return station;
    }
}
