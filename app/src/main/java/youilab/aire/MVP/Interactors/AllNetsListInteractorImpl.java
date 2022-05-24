package youilab.aire.MVP.Interactors;

import youilab.aire.IntegrASCore.Nets.NetsDAOImpl;
import youilab.aire.MVP.Interfaces.AllNetsList.AllNetsListInteractor;
import youilab.aire.MVP.Interfaces.AllNetsList.OnAllNetsListFinishListener;

public class AllNetsListInteractorImpl implements AllNetsListInteractor {

    private NetsDAOImpl netsDAO;

    public AllNetsListInteractorImpl(){
        netsDAO = new NetsDAOImpl();
    }

    @Override
    public void getAllNetsFromJSONFile(OnAllNetsListFinishListener listener) {
        listener.returnAllNets(netsDAO.getAllNets());
    }
}
