package youilab.aire.AppTools;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


public class PermissionsIntegrAS {
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 2;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    private static boolean bndLocation=false;
    private static boolean bndWriteES = false;


    private static void checkPermission(Activity paramActivity, String paramString, int paramInt)
    {
        if (ContextCompat.checkSelfPermission(paramActivity,paramString) != PackageManager.PERMISSION_GRANTED)
        {
           /* if (ActivityCompat.shouldShowRequestPermissionRationale(paramActivity, paramString)) {

            }else
            {*/
                ActivityCompat.requestPermissions(paramActivity, new String[] { paramString }, paramInt);
            //}
        }
        else
        {
            if (paramInt == 1) {
                bndWriteES = true;
            }
            if (paramInt == 2) {
                bndLocation = true;
            }

        }

        return;
    }



    public static boolean accesFineLocationPermission(Activity activity)
    {
        bndLocation = false;
        checkPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION,2);

        return bndLocation;
    }

    public static boolean accessWriteESPermission(Activity activity)
    {
        bndWriteES = false;
        checkPermission(activity,Manifest.permission.WRITE_EXTERNAL_STORAGE, 1);
        return bndWriteES;
    }


}
