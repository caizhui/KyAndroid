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

    public SweetAlertDialogUtil(Context context) {
        this.context = context;
        sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
    }

    /**
     * 弹出loading框
     */
    public void loadAlertDialog() {
        sweetAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        sweetAlertDialog.setTitleText("Loading");
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.show();
    }

    /**
     * 弹出loading框
     */
    public void loadAlertDialog(String titleText) {
        sweetAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        sweetAlertDialog.setTitleText(titleText);
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.show();
    }

    /**
     * 取消弹出框
     */
    public void dismissAlertDialog() {
        sweetAlertDialog.dismiss();
    }

    /**
     * 弹出询问对话框架
     *
     * @param titleText
     * @param contentText
     * @param confirmClicklistener
     */
    public void showAlertDialogConfirm(String titleText, String contentText,SweetAlertDialog.OnSweetClickListener confirmClicklistener) {
        sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.CUSTOM_IMAGE_TYPE);
        sweetAlertDialog.setTitleText(titleText);
        sweetAlertDialog.setContentText(contentText);
        sweetAlertDialog.setCancelText("取消");
        sweetAlertDialog.setConfirmText("确定");
        sweetAlertDialog.showCancelButton(true);
        sweetAlertDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                sDialog.cancel();
            }
        });
        sweetAlertDialog.setConfirmClickListener(confirmClicklistener);
        sweetAlertDialog.show();
    }
}
