package youilab.aire.IntegrASCore.Nets;

import android.location.Location;
import youilab.aire.IntegrASCore.Stations.Station;

import java.util.List;

public interface NetsDAO {
    List<Net> getAllNets();
    Net getNetByCode(String netId);
    Station getStationByCode(Net net, String stationId);
    Net getNetByLocation(Location myLocation);
}
