package com.arfeenkhan.registerationappforUser.Utils;

import android.app.Service;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class CheckInternet {
    private Context context;

    public CheckInternet(Context context) {
        this.context = context;
    }

    public boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Service.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo info = connectivityManager.getActiveNetworkInfo();
//            int netSubType = info.getSubtype();
//            if (netSubType == TelephonyManager.NETWORK_TYPE_GPRS ||
//                    netSubType == TelephonyManager.NETWORK_TYPE_EDGE ||
//                    netSubType == TelephonyManager.NETWORK_TYPE_1xRTT)
                if (info != null) {
                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                    if (info.getState() == NetworkInfo.State.DISCONNECTED) {
                        Toast.makeText(context, "No Intetnet", Toast.LENGTH_SHORT).show();
                    }
                    if (info.getState() == NetworkInfo.State.DISCONNECTING) {
                        Toast.makeText(context, "Network is slow", Toast.LENGTH_SHORT).show();
                    }
                }
        }

        return false;
    }
}
