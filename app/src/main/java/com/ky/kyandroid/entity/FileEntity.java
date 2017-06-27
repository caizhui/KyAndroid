package com.ky.kyandroid.entity;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by Caizhui on 2017/6/27.
 */

public class FileEntity implements Serializable{

    /**
     * 图片
     */
    public Bitmap bitmap;


    /**
     * 从后台哪来的文件url链接
     */
    public String fileUrl;

    /**
     * 文件描述
     */
    public  String fileMc;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getFileMc() {
        return fileMc;
    }

    public void setFileMc(String fileMc) {
        this.fileMc = fileMc;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }
}
