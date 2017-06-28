package com.ky.kyandroid.entity;

import android.graphics.Bitmap;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * Created by Caizhui on 2017/6/27.
 */
@Table(name = "t_file")
public class FileEntity implements Serializable{

    @Column(name = "uuid",isId = true,autoGen = true)
    private int uuid;

    /**
     * 图片
     */
    public Bitmap bitmap;


    /**
     * 从后台哪来的文件url链接
     */
    @Column(name = "fileUrl")
    public String fileUrl;

    /**
     * 文件描述
     */
    @Column(name = "fileMs")
    public  String fileMs;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public int getUuid() {
        return uuid;
    }

    public void setUuid(int uuid) {
        this.uuid = uuid;
    }

    public String getFileMs() {
        return fileMs;
    }

    public void setFileMs(String fileMs) {
        this.fileMs = fileMs;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }
}
