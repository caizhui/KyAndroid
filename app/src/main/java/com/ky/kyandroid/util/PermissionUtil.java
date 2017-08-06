package com.ky.kyandroid.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by msi on 2017/8/6.
 */

public class PermissionUtil {

    /**
     * 询问权限
     */
    public static void requestPermissions(Context context, String[] permissions, int request_code) {
        List<String> ack_permissiom_list = new ArrayList<String>();
        for (String permission : permissions) {
            int location_permission = ActivityCompat.checkSelfPermission(context, permission);
            if (location_permission != PackageManager.PERMISSION_GRANTED) {
                ack_permissiom_list.add(permission);
            }
        }
        if (ack_permissiom_list.size() > 0) {
            ActivityCompat.requestPermissions((Activity) context, ack_permissiom_list.toArray(new String[]{}), request_code);
        }
    }

}
