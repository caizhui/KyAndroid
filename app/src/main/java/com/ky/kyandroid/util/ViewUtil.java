package com.ky.kyandroid.util;

import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Administrator on 2017-3-3.
 */

public class ViewUtil {

    public static String getString(EditText et){
        if(et != null){
            return et.getText().toString().trim();
        }
        return null;
    }

    public static void setText(TextView tv, String s){
        if(tv != null){
            tv.setText(s);
        }
    }

    public static void setClickListener(View v, View.OnClickListener listener){
        if(v != null){
            v.setOnClickListener(listener);
        }
    }

    public static void setFocusable(View v,boolean b){
        if(v != null){
            v.setFocusable(b);
        }
    }

    public static boolean isChecked(CheckBox checkBox){
        if(checkBox != null){
            return checkBox.isChecked();
        }
        return false;
    }
}
