package com.orozco.netreport.handler.network;

import com.facebook.network.connectionclass.ConnectionClassManager;
import com.facebook.network.connectionclass.ConnectionQuality;
import com.facebook.network.connectionclass.DeviceBandwidthSampler;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.inject.Inject;

import rx.Observable;

/**
 * Paul Sydney Orozco (@xtrycatchx) on 4/2/17.
 */

public class NetworkHelper {

    private String testUrl = "http://bloodlifeph.herokuapp.com/img/graph_share_bloodlife.jpg";
    private int attempts = 0;
    private ConnectionQuality mConnectionClass = ConnectionQuality.UNKNOWN;
    private ConnectionClassManager mConnectionClassManager;
    private DeviceBandwidthSampler mDeviceBandwidthSampler;
    private ConnectionChangedListener mListener;

    @Inject
    public NetworkHelper() {
    }

    public Observable<String> executeNetworkTest() {

        mConnectionClassManager = ConnectionClassManager.getInstance();
        mDeviceBandwidthSampler = DeviceBandwidthSampler.getInstance();
        mListener = new ConnectionChangedListener();
        mConnectionClassManager.register(mListener);
        testDownload();
        while (mConnectionClass == ConnectionQuality.UNKNOWN && attempts < 10) {
            attempts++;
            testDownload();
        }
        return Observable.just(mConnectionClass.toString());
    }

    private void testDownload() {
        mDeviceBandwidthSampler.startSampling();
        try {
            URLConnection connection = new URL(testUrl).openConnection();
            connection.setUseCaches(false);
            connection.connect();
            InputStream input = connection.getInputStream();
            try {
                byte[] buffer = new byte[1024];
                while (input.read(buffer) != -1) {
                }
            } finally {
                input.close();
            }
        } catch (IOException e) {

        }
        mDeviceBandwidthSampler.stopSampling();
    }

    /**
     * Listener to update the UI upon connectionclass change.
     */
    private class ConnectionChangedListener
            implements ConnectionClassManager.ConnectionClassStateChangeListener {

        @Override
        public void onBandwidthStateChange(ConnectionQuality bandwidthState) {
            mConnectionClass = bandwidthState;
        }
    }
}
