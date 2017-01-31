package com.stdnull.runmap.map;

import android.os.SystemClock;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.stdnull.runmap.GlobalApplication;
import com.stdnull.runmap.bean.TrackPoint;
import com.stdnull.runmap.common.CFLog;
import com.stdnull.runmap.common.RMConfiguration;
import com.stdnull.runmap.utils.SystemUtils;

import java.util.List;

/**
 * Created by chen on 2017/1/28.
 */

public class AmLocationHelper{
    private float mLatestIncreasedDistance = 0;
    private GeocodeSearch mGeocodeSearch;
    private Long mLatestUpdateTime;

    public AmLocationHelper(){
        mGeocodeSearch = new GeocodeSearch(GlobalApplication.getAppContext());
    }
    public boolean shouldAddLatLng(List<TrackPoint> trackPointList, LatLng latLng, float speed){
        if(trackPointList.isEmpty()) {
            mLatestUpdateTime = SystemClock.elapsedRealtime();
            CFLog.e(AmLocationManager.TAG,"Add new data = " + latLng.toString());
            mLatestIncreasedDistance = 0;
            return true;
        }
        else{
            TrackPoint point = trackPointList.get(trackPointList.size()-1);
            float distance = AMapUtils.calculateLineDistance(latLng,new LatLng(point.getLatitude(),point.getLongitude()));
            if(distance < RMConfiguration.DRAW_DISTANCE){
                mLatestIncreasedDistance = 0;
                CFLog.e(AmLocationManager.TAG,"to closed , don't need , distance = " + distance);
                return false;
            }
            if(!isErrorLaglng(distance,speed)){
                mLatestUpdateTime = SystemClock.elapsedRealtime();
                mLatestIncreasedDistance = distance;
                CFLog.i(AmLocationManager.TAG,"Add new data = " + latLng.toString());
                return true;
            }
            return false;
        }
    }


    private boolean isErrorLaglng(float distance,float speed){
        double interval = (SystemClock.elapsedRealtime() - mLatestUpdateTime)/1000.0;
        float v = (float) (distance/interval);
        if(v > RMConfiguration.MAX_SPEED || v > speed * 1.5){
            CFLog.e(AmLocationManager.TAG,"error data, don't need to add, speed ="+v +" ,"+speed);
            return true;
        }
        return false;

    }

    public float getLatestIncreasedDistance(){
        return mLatestIncreasedDistance;
    }


    public RegeocodeAddress regeocodeAddress(LatLonPoint latLonPoint){
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200,GeocodeSearch.AMAP);
        try {
            return mGeocodeSearch.getFromLocation(query);
        } catch (AMapException e) {
            CFLog.e(this.getClass().getName(),"Regeocode failed");
        }
        return null;
    }


}
