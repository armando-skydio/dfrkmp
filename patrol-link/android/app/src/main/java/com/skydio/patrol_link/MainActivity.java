package com.skydio.patrol_link;

import android.os.Bundle;

import com.getcapacitor.BridgeActivity;

public class MainActivity extends BridgeActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getIntent().getExtras();
        if (b != null) {
            String token = b.getString("token", "");
            bridge.triggerJSEvent("token", "document", "{ 'token': '" + token + "' }");
        }

    }
    public void setToken() {
    }
}
