package youilab.aire.MVP.Interfaces.CurrentNet;

import android.location.Location;

public interface CurrentNetInteractor {
    void getCurrenNetByLocation(Location myLocation,OnCurrentNetFinishListener listener);
}
