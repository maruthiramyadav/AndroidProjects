package com.devobal.ogabuzz.SpecialClasses;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.maps.android.SphericalUtil;

/**
 * Created by Devobal on 2/1/2019 on 8:54 PM.
 */
public class Utility {

    public static LatLngBounds toBounds(LatLng center, double radius) {
        LatLng southwest = SphericalUtil.computeOffset(center, radius * Math.sqrt(2.0), 225);
        LatLng northeast = SphericalUtil.computeOffset(center, radius * Math.sqrt(2.0), 45);
        return new LatLngBounds(southwest, northeast);
    }
    public static boolean checkValidStringOrNot(String str){
        if (str!=null) {
            str = str.trim();
        }
        if (str == null || str.equals("") || str.equals("null")){
            return false;
        }else {
            return true;
        }
    }
    public static boolean isNotAnValidString(String string){
        if (!string.equals("0") && !string.equals(null) && !string.equals("null") && !string.equals("")){
            return false;
        }
        return true;
    }

    public static boolean isValidString(String string){
        if (!string.equals("0") && !string.equals(null) && !string.equals("null") && !string.equals("")){
            return true;
        }
        return false;
    }
    public static boolean isValidInteger(Integer integer){
        if (integer!=null){
            return true;
        }
        return false;
    }
}
