package youilab.aire.MVP.Interfaces.StationInformation;

import youilab.aire.IntegrASCore.Nets.Net;
import youilab.aire.IntegrASCore.Stations.Station;

public interface StationInformationView {
    void StationInformation(Station station);
    void NetInformation(Net net);
}
