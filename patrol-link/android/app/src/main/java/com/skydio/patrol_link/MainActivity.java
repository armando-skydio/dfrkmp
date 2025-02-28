package com.skydio.patrol_link;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import com.getcapacitor.BridgeActivity;

public class MainActivity extends BridgeActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        extractUpdate(getIntent());
        registerReceiver(new TokenBroadCastListener(), new IntentFilter("token"), Context.RECEIVER_EXPORTED);
    }

    public void setToken() {
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        extractUpdate(intent);
    }



    private void extractUpdate(Intent intent) {
        Bundle b = intent.getExtras();
        if (b != null) {
            String token = b.getString("token", "");
            double latitude = b.getDouble("latitude",0.0);
            double longitude = b.getDouble("longitude",0.0);
            double altitude = b.getDouble("altitude",0.0);
            Float speed = b.getFloat("speed",0F);
            Float bearing = b.getFloat("bearing",0F);
            Float accuracy = b.getFloat("accuracy",0F);
            bridge.triggerJSEvent("token", "document", "{ 'token': '" + token + "' }");
            Log.e("PatrolLinkCapacitor", "Token: " + token);
            Log.e("PatrolLinkCapacitor", "Location: " + " lat: " + latitude + " lng: " + longitude + " alt: " + altitude);
        }
    }

    class TokenBroadCastListener extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            extractUpdate(intent);
        }
    }

}
