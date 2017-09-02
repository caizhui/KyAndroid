package com.ky.kyandroid.bean;

import android.graphics.Bitmap;

import java.util.Map;

/**
 * Created by msi on 2017/8/20.
 */
public class BitmapInfo {

    /**
     * 缩略图
     */
    private Bitmap bitmap;

    /**
     * 缩略图信息
     */
    Map<String,String> fileInfoMap;


    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Map<String, String> getFileInfoMap() {
        return fileInfoMap;
    }

    public void setFileInfoMap(Map<String, String> fileInfoMap) {
        this.fileInfoMap = fileInfoMap;
    }
}
