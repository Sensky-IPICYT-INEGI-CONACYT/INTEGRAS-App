package youilab.aire.MVP.Interfaces.StationInformation;

import youilab.aire.IntegrASCore.Nets.Net;
import youilab.aire.IntegrASCore.Stations.Station;

public interface OnStationInformationFinishListener {
    void returnStationInformation(Station station, Net net);
}
