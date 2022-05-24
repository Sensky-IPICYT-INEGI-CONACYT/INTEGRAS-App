package youilab.aire.MVP.Interactors;

import android.location.Location;
import youilab.aire.IntegrASCore.Nets.Net;
import youilab.aire.IntegrASCore.Nets.NetsDAOImpl;
import youilab.aire.MVP.Interfaces.CurrentNet.CurrentNetInteractor;
import youilab.aire.MVP.Interfaces.CurrentNet.OnCurrentNetFinishListener;

public class CurrentNetInteractorImpl implements CurrentNetInteractor {


    private NetsDAOImpl netsDAO;


    public  CurrentNetInteractorImpl() {
        netsDAO = new NetsDAOImpl();
    }

    @Override
    public void getCurrenNetByLocation(Location myLocation, OnCurrentNetFinishListener listener) {
        Net myNetStation = null;
       myNetStation = netsDAO.getNetByLocation(myLocation);
        listener.returnCurrentNet(myNetStation);
    }

}
