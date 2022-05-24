package youilab.aire.MVP.Presenters;

import youilab.aire.IntegrASCore.Nets.Net;
import youilab.aire.MVP.Interactors.AllNetsListInteractorImpl;
import youilab.aire.MVP.Interfaces.AllNetsList.AllNetsListInteractor;
import youilab.aire.MVP.Interfaces.AllNetsList.AllNetsListPresenter;
import youilab.aire.MVP.Interfaces.AllNetsList.AllNetsListView;
import youilab.aire.MVP.Interfaces.AllNetsList.OnAllNetsListFinishListener;

import java.util.List;

public class AllNetsListPresenterImpl implements AllNetsListPresenter , OnAllNetsListFinishListener {

    private AllNetsListView view = null;
    private AllNetsListInteractor allNetsListInteractor= null;

    public AllNetsListPresenterImpl(AllNetsListView view ){
        this.view = view;
        this.allNetsListInteractor = new AllNetsListInteractorImpl();
    }

    @Override
    public void getAllNets() {
        this.allNetsListInteractor.getAllNetsFromJSONFile(this);
    }

    @Override
    public void returnAllNets(List<Net> netList) {
        if(this.view != null){
            this.view.AllNets(netList);
        }
    }
}
