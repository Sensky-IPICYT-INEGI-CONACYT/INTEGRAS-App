package youilab.aire.MVP.Presenters;

import youilab.aire.IntegrASCore.Nets.Net;
import youilab.aire.IntegrASCore.Stations.Station;
import youilab.aire.MVP.Interactors.StationInformationInteractorImpl;
import youilab.aire.MVP.Interfaces.StationInformation.OnStationInformationFinishListener;
import youilab.aire.MVP.Interfaces.StationInformation.StationInformationInteractor;
import youilab.aire.MVP.Interfaces.StationInformation.StationInformationPresenter;
import youilab.aire.MVP.Interfaces.StationInformation.StationInformationView;

public class StationInformationPresenterImpl implements StationInformationPresenter, OnStationInformationFinishListener {

    private StationInformationView view = null;
    private StationInformationInteractor stationInformationInteractor = null;

    public StationInformationPresenterImpl(StationInformationView view){
        this.view = view;
        this.stationInformationInteractor = new StationInformationInteractorImpl();
    }

    @Override
    public void returnStationInformation(Station station, Net net) {
        if(this.view != null){
            this.view.NetInformation(net);
            this.view.StationInformation(station);
        }
    }

    @Override
    public void getStationInformation(String netId, String stationId) {
        this.stationInformationInteractor.getStationInformationFromJSONData(netId,stationId,this);
    }
}
