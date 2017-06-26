package com.ky.kyandroid.util;

import android.content.Context;
import android.graphics.Color;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Caizhui on 2017/6/24.
 */

public class SweetAlertDialogUtil {

    public SweetAlertDialog sweetAlertDialog;

    private Context context;

    public SweetAlertDialogUtil(Context context){
        this.context=context;
        sweetAlertDialog  = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
    }

    /**
     * 弹出loading框
     */
    public  void loadAlertDialog(){
        sweetAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        sweetAlertDialog.setTitleText("Loading");
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.show();
    }

    /**
     * 弹出loading框
     */
    public  void loadAlertDialog(String titleText){
        sweetAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        sweetAlertDialog.setTitleText(titleText);
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.show();
    }

    /**
     * 取消弹出框
     */
    public void dismissAlertDialog(){
        sweetAlertDialog.dismiss();
    }
}
