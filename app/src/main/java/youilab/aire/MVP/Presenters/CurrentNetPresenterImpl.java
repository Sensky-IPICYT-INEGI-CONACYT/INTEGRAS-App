package youilab.aire.MVP.Presenters;

import android.location.Location;
import youilab.aire.IntegrASCore.Nets.Net;
import youilab.aire.MVP.Interactors.CurrentNetInteractorImpl;
import youilab.aire.MVP.Interfaces.CurrentNet.CurrentNetInteractor;
import youilab.aire.MVP.Interfaces.CurrentNet.CurrentNetPresenter;
import youilab.aire.MVP.Interfaces.CurrentNet.CurrentNetView;
import youilab.aire.MVP.Interfaces.CurrentNet.OnCurrentNetFinishListener;

public class CurrentNetPresenterImpl implements CurrentNetPresenter, OnCurrentNetFinishListener {

    private CurrentNetView view = null;
    private CurrentNetInteractor currentNetInteractor= null;

    public CurrentNetPresenterImpl(CurrentNetView view){
        this.view = view;
        this.currentNetInteractor = new CurrentNetInteractorImpl();
    }

    @Override
    public void getCurrentNet(Location myLocation) {
        currentNetInteractor.getCurrenNetByLocation(myLocation,this);
    }

    @Override
    public void returnCurrentNet(Net stationsNet) {
        if(this.view != null){
            view.currentNet(stationsNet);
        }
    }
}
