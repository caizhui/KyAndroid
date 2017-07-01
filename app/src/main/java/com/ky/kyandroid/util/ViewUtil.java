package com.ky.kyandroid.util;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ky.kyandroid.view.BadgeView;

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

    public static BadgeView createBeView(Context context, ImageView containerView){
        BadgeView badge = new BadgeView(context, containerView);
        badge.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
        badge.setBadgeBackgroundColor(Color.parseColor("#FF0033"));
        badge.setVisibility(View.GONE);
        return badge;
    }
}
