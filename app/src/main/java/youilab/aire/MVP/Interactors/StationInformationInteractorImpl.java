package youilab.aire.MVP.Interactors;

import youilab.aire.IntegrASCore.Nets.Net;
import youilab.aire.IntegrASCore.Nets.NetsDAOImpl;
import youilab.aire.IntegrASCore.Stations.Station;
import youilab.aire.MVP.Interfaces.StationInformation.OnStationInformationFinishListener;
import youilab.aire.MVP.Interfaces.StationInformation.StationInformationInteractor;

public class StationInformationInteractorImpl implements StationInformationInteractor {
    private NetsDAOImpl netsDAO;

    public StationInformationInteractorImpl(){
        netsDAO = new NetsDAOImpl();
    }

    @Override
    public void getStationInformationFromJSONData(String netId, String stationId, OnStationInformationFinishListener listener) {
        Net myNet = netsDAO.getNetByCode(netId);
        Station myStation = netsDAO.getStationByCode(myNet,stationId);
        listener.returnStationInformation(myStation,myNet);
    }
}
