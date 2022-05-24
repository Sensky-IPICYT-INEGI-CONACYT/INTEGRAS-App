package youilab.aire.MVP.Interfaces.StationInformation;

public interface StationInformationInteractor {
    void getStationInformationFromJSONData(String netId,String stationId,OnStationInformationFinishListener listener);
}
